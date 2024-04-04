package com.toLearn.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toLearn.Mapper.CommentLikeMapper;
import com.toLearn.Model.Domain.CommentLike;
import com.toLearn.Service.CommentLikeService;
import org.springframework.stereotype.Service;

/**
 * 博文评论服务实现
 * @author BraumAce
 */
@Service
public class CommentLikeServiceImpl extends ServiceImpl<CommentLikeMapper, CommentLike>
        implements CommentLikeService {

}




