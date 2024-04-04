package com.toLearn.Service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toLearn.Common.ErrorCode;
import com.toLearn.Mapper.BlogCommentsMapper;
import com.toLearn.Model.Domain.*;
import com.toLearn.Model.Enums.MessageTypeEnum;
import com.toLearn.Model.Request.AddCommentRequest;
import com.toLearn.Model.VO.BlogCommentsVO;
import com.toLearn.Model.VO.BlogVO;
import com.toLearn.Model.VO.UserVO;
import com.toLearn.Service.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 博文评论服务实现
 * @author BraumAce
 */
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments>
        implements BlogCommentsService {

    @Resource
    private UserService userService;

    @Resource
    private BlogService blogService;

    @Resource
    private CommentLikeService commentLikeService;

    @Resource
    private MessageService messageService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Value("${super.qiniu.url:null}")
    private String qiniuUrl;

    /**
     * 添加评论
     * @param addCommentRequest 添加评论请求
     * @param userId            用户id
     */
    @Override
    @Transactional
    public void addComment(AddCommentRequest addCommentRequest, Long userId) {
        BlogComments blogComments = new BlogComments();
        blogComments.setUserId(userId);
        blogComments.setBlogId(addCommentRequest.getBlogId());
        blogComments.setContent(addCommentRequest.getContent());
        blogComments.setLikedNum(0);
        blogComments.setStatus(0);
        this.save(blogComments);
        Blog blog = blogService.getById(addCommentRequest.getBlogId());
        blogService.update().eq("id", addCommentRequest.getBlogId())
                .set("comments_num", blog.getCommentsNum() + 1).update();
    }

    /**
     * 列出评论
     * @param blogId 博客id
     * @param userId 用户id
     */
    @Override
    public List<BlogCommentsVO> listComments(long blogId, long userId) {
        LambdaQueryWrapper<BlogComments> blogCommentsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogCommentsLambdaQueryWrapper.eq(BlogComments::getBlogId, blogId);
        List<BlogComments> blogCommentsList = this.list(blogCommentsLambdaQueryWrapper);
        return blogCommentsList.stream().map((comment) -> {
            BlogCommentsVO blogCommentsVO = new BlogCommentsVO();
            BeanUtils.copyProperties(comment, blogCommentsVO);
            User user = userService.getById(comment.getUserId());
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            blogCommentsVO.setCommentUser(userVO);
            LambdaQueryWrapper<CommentLike> commentLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            commentLikeLambdaQueryWrapper.eq(CommentLike::getCommentId, comment.getId())
                    .eq(CommentLike::getUserId, userId);
            long count = commentLikeService.count(commentLikeLambdaQueryWrapper);
            blogCommentsVO.setIsLiked(count > 0);
            return blogCommentsVO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取评论
     * @param commentId 议论id
     * @param userId    用户id
     */
    @Override
    public BlogCommentsVO getComment(long commentId, Long userId) {
        BlogComments comments = this.getById(commentId);
        if (comments == null) {
            return null;
        }
        BlogCommentsVO blogCommentsVO = new BlogCommentsVO();
        BeanUtils.copyProperties(comments, blogCommentsVO);
        LambdaQueryWrapper<CommentLike> commentLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLikeLambdaQueryWrapper.eq(CommentLike::getUserId, userId).eq(CommentLike::getCommentId, commentId);
        long count = commentLikeService.count(commentLikeLambdaQueryWrapper);
        blogCommentsVO.setIsLiked(count > 0);
        return blogCommentsVO;
    }

    /**
     * 点赞评论
     * @param commentId 议论id
     * @param userId    用户id
     */
    @Override
    @Transactional
    public void likeComment(Long commentId, Long userId) {
        RLock lock = redissonClient.getLock(COMMENTS_LIKE_LOCK + commentId + ":" + userId);
        try {
            if (lock.tryLock(DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, TimeUnit.MILLISECONDS)) {
                BlogComments comments = this.getById(commentId);
                if (comments == null) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "评论不存在");
                }
                LambdaQueryWrapper<CommentLike> commentLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
                commentLikeLambdaQueryWrapper.eq(CommentLike::getCommentId, commentId)
                        .eq(CommentLike::getUserId, userId);
                long count = commentLikeService.count(commentLikeLambdaQueryWrapper);
                if (count == 0) {
                    doLikeComment(commentId, userId);
                } else {
                    commentLikeService.remove(commentLikeLambdaQueryWrapper);
                    doUnLikeComment(commentId, userId);
                }
            }
        } catch (Exception e) {
            log.error("LikeBlog error", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    /**
     * 喜欢评论
     * @param commentId 评论id
     * @param userId    用户id
     */
    public void doLikeComment(Long commentId, Long userId) {
        CommentLike commentLike = new CommentLike();
        commentLike.setCommentId(commentId);
        commentLike.setUserId(userId);
        commentLikeService.save(commentLike);
        BlogComments blogComments = this.getById(commentId);
        this.update().eq("id", commentId)
                .set("liked_num", blogComments.getLikedNum() + 1)
                .update();
        String likeNumKey = MESSAGE_LIKE_NUM_KEY + blogComments.getUserId();
        Boolean hasKey = stringRedisTemplate.hasKey(likeNumKey);
        if (Boolean.TRUE.equals(hasKey)) {
            stringRedisTemplate.opsForValue().increment(likeNumKey);
        } else {
            stringRedisTemplate.opsForValue().set(likeNumKey, "1");
        }
        Message message = new Message();
        message.setType(MessageTypeEnum.BLOG_COMMENT_LIKE.getValue());
        message.setFromId(userId);
        message.setToId(blogComments.getUserId());
        message.setData(String.valueOf(blogComments.getId()));
        messageService.save(message);
    }

    /**
     * 取消喜欢评论
     * @param commentId 评论id
     * @param userId    用户id
     */
    public void doUnLikeComment(Long commentId, Long userId) {
        BlogComments blogComments = this.getById(commentId);
        this.update().eq("id", commentId)
                .set("liked_num", blogComments.getLikedNum() - 1)
                .update();
        LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>();
        messageQueryWrapper
                .eq(Message::getType, MessageTypeEnum.BLOG_COMMENT_LIKE.getValue())
                .eq(Message::getFromId, userId)
                .eq(Message::getToId, blogComments.getUserId())
                .eq(Message::getData, String.valueOf(blogComments.getId()));
        messageService.remove(messageQueryWrapper);
        String likeNumKey = MESSAGE_LIKE_NUM_KEY + blogComments.getUserId();
        String upNumStr = stringRedisTemplate.opsForValue().get(likeNumKey);
        if (!StrUtil.isNullOrUndefined(upNumStr) && Long.parseLong(upNumStr) != 0) {
            stringRedisTemplate.opsForValue().decrement(likeNumKey);
        }
    }

    /**
     * 删除评论
     * @param id      id
     * @param userId  用户id
     * @param isAdmin 是否为管理员
     */
    @Override
    @Transactional
    public void deleteComment(Long id, Long userId, boolean isAdmin) {
        BlogComments blogComments = this.getById(id);
        if (isAdmin) {
            this.removeById(id);
            Integer commentsNum = blogService.getById(blogComments.getBlogId()).getCommentsNum();
            blogService.update().eq("id", blogComments.getBlogId()).set("comments_num", commentsNum - 1).update();
            return;
        }
        if (blogComments == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!blogComments.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        this.removeById(id);
        Integer commentsNum = blogService.getById(blogComments.getBlogId()).getCommentsNum();
        blogService.update().eq("id", blogComments.getBlogId()).set("comments_num", commentsNum - 1).update();
    }

    /**
     * 列出我的评论
     * @param id id
     */
    @Override
    public List<BlogCommentsVO> listMyComments(Long id) {
        LambdaQueryWrapper<BlogComments> blogCommentsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogCommentsLambdaQueryWrapper.eq(BlogComments::getUserId, id);
        List<BlogComments> blogCommentsList = this.list(blogCommentsLambdaQueryWrapper);
        return blogCommentsList.stream().map((item) -> {
            BlogCommentsVO blogCommentsVO = new BlogCommentsVO();
            BeanUtils.copyProperties(item, blogCommentsVO);
            User user = userService.getById(item.getUserId());
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            blogCommentsVO.setCommentUser(userVO);

            Long blogId = blogCommentsVO.getBlogId();
            Blog blog = blogService.getById(blogId);
            BlogVO blogVO = new BlogVO();
            BeanUtils.copyProperties(blog, blogVO);
            String images = blogVO.getImages();
            if (images == null) {
                blogVO.setCoverImage(null);
            } else {
                String[] imgStr = images.split(",");
                blogVO.setCoverImage(qiniuUrl + imgStr[0]);
            }
            Long authorId = blogVO.getUserId();
            User author = userService.getById(authorId);
            UserVO authorVO = new UserVO();
            BeanUtils.copyProperties(author, authorVO);
            blogVO.setAuthor(authorVO);

            blogCommentsVO.setBlog(blogVO);

            LambdaQueryWrapper<CommentLike> commentLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            commentLikeLambdaQueryWrapper.eq(CommentLike::getUserId, id).eq(CommentLike::getCommentId, item.getId());
            long count = commentLikeService.count(commentLikeLambdaQueryWrapper);
            blogCommentsVO.setIsLiked(count > 0);
            return blogCommentsVO;
        }).collect(Collectors.toList());
    }

    /**
     * 分页我的评论
     * @param id          id
     * @param currentPage 当前页码
     */
    @Override
    public Page<BlogCommentsVO> pageMyComments(Long id, Long currentPage) {
        LambdaQueryWrapper<BlogComments> blogCommentsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        blogCommentsLambdaQueryWrapper.eq(BlogComments::getUserId, id);
        Page<BlogComments> blogCommentsPage = this.page(new Page<>(currentPage, PAGE_SIZE),
                blogCommentsLambdaQueryWrapper);
        if (blogCommentsPage == null || blogCommentsPage.getSize() == 0) {
            return new Page<>();
        }
        Page<BlogCommentsVO> blogCommentsVoPage = new Page<>();
        BeanUtils.copyProperties(blogCommentsPage, blogCommentsVoPage);
        List<BlogCommentsVO> blogCommentsVOList = blogCommentsPage.getRecords().stream().map((item) -> {
            BlogCommentsVO blogCommentsVO = new BlogCommentsVO();
            BeanUtils.copyProperties(item, blogCommentsVO);
            User user = userService.getById(item.getUserId());
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            blogCommentsVO.setCommentUser(userVO);
            Long blogId = blogCommentsVO.getBlogId();
            Blog blog = blogService.getById(blogId);
            if (blog == null) {
                return null;
            }
            BlogVO blogVO = new BlogVO();
            BeanUtils.copyProperties(blog, blogVO);
            String images = blogVO.getImages();
            if (images == null) {
                blogVO.setCoverImage(null);
            } else {
                String[] imgStr = images.split(",");
                blogVO.setCoverImage(qiniuUrl + imgStr[0]);
            }
            Long authorId = blogVO.getUserId();
            User author = userService.getById(authorId);
            UserVO authorVO = new UserVO();
            BeanUtils.copyProperties(author, authorVO);
            blogVO.setAuthor(authorVO);
            blogCommentsVO.setBlog(blogVO);
            LambdaQueryWrapper<CommentLike> commentLikeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            commentLikeLambdaQueryWrapper.eq(CommentLike::getUserId, id).eq(CommentLike::getCommentId, item.getId());
            long count = commentLikeService.count(commentLikeLambdaQueryWrapper);
            blogCommentsVO.setIsLiked(count > 0);
            return blogCommentsVO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        blogCommentsVoPage.setRecords(blogCommentsVOList);
        return blogCommentsVoPage;
    }
}




