package com.toLearn.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toLearn.Mapper.FollowMapper;
import com.toLearn.Model.Domain.Follow;
import com.toLearn.Model.Domain.User;
import com.toLearn.Model.VO.UserVO;
import com.toLearn.Service.FollowService;
import com.toLearn.Service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.toLearn.Constants.SystemConstants.PAGE_SIZE;

/**
 * 关注服务实现
 * @author BraumAce
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow>
        implements FollowService {

    @Resource
    @Lazy
    private UserService userService;

    /**
     * 关注用户
     * @param followUserId 关注用户id
     * @param userId       用户id
     */
    @Override
    public void followUser(Long followUserId, Long userId) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFollowUserId, followUserId).eq(Follow::getUserId, userId);
        long count = this.count(followLambdaQueryWrapper);
        if (count == 0) {
            Follow follow = new Follow();
            follow.setFollowUserId(followUserId);
            follow.setUserId(userId);
            this.save(follow);
        } else {
            this.remove(followLambdaQueryWrapper);
        }
    }

    /**
     * 关注我的粉丝列表
     * @param userId 用户id
     * @return
     */
    @Override
    public List<UserVO> listFans(Long userId) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFollowUserId, userId);
        List<Follow> list = this.list(followLambdaQueryWrapper);
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        List<User> userList = list.stream().map((follow -> userService.getById(follow.getUserId())))
                .filter(Objects::nonNull).collect(Collectors.toList());
        return userList.stream().map((user) -> this.getUserFollowInfo(user, userId)).collect(Collectors.toList());
    }

    /**
     * 我关注的人列表
     * @param userId 用户id
     * @return
     */
    @Override
    public List<UserVO> listMyFollow(Long userId) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getUserId, userId);
        List<Follow> list = this.list(followLambdaQueryWrapper);
        List<User> userList = list.stream().map((follow -> userService.getById(follow.getFollowUserId())))
                .collect(Collectors.toList());
        return userList.stream().map((user) -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setIsFollow(true);
            return userVO;
        }).collect(Collectors.toList());
    }

    /**
     * 分页我关注的人
     * @param userId      用户id
     * @param currentPage 当前页码
     * @return
     */
    @Override
    public Page<UserVO> pageMyFollow(Long userId, String currentPage) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getUserId, userId);
        Page<Follow> followPage = this.page(
                new Page<>(Long.parseLong(currentPage), PAGE_SIZE),
                followLambdaQueryWrapper);
        if (followPage == null || followPage.getSize() == 0) {
            return new Page<>();
        }
        Page<UserVO> userVoPage = new Page<>();
        List<User> userList = followPage.getRecords().stream()
                .map((follow -> userService.getById(follow.getFollowUserId())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<UserVO> userVOList = userList.stream().map((user) -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setIsFollow(true);
            return userVO;
        }).collect(Collectors.toList());
        return userVoPage.setRecords(userVOList);
    }

    /**
     * 分页我的粉丝
     * @param userId      用户id
     * @param currentPage 当前页码
     * @return
     */
    @Override
    public Page<UserVO> pageFans(Long userId, String currentPage) {
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFollowUserId, userId);
        Page<Follow> followPage = this.page(
                new Page<>(Long.parseLong(currentPage),
                        PAGE_SIZE),
                followLambdaQueryWrapper);
        if (followPage == null || followPage.getSize() == 0) {
            return new Page<>();
        }
        Page<UserVO> userVoPage = new Page<>();
        BeanUtils.copyProperties(followPage, userVoPage);
        List<User> userList = followPage.getRecords().stream()
                .map((follow -> userService.getById(follow.getUserId())))
                .filter(Objects::nonNull).collect(Collectors.toList());
        List<UserVO> userVOList = userList.stream()
                .map((user) -> this.getUserFollowInfo(user, userId))
                .collect(Collectors.toList());
        userVoPage.setRecords(userVOList);
        return userVoPage;
    }

    /**
     * 获取用户关注信息
     * @param user   用户
     * @param userId 用户id
     * @return
     */
    @Override
    public UserVO getUserFollowInfo(User user, long userId) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper
                .eq(Follow::getUserId, userId)
                .eq(Follow::getFollowUserId, userVO.getId());
        long count = this.count(followLambdaQueryWrapper);
        userVO.setIsFollow(count > 0);
        return userVO;
    }
}




