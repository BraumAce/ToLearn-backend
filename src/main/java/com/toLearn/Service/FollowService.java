package com.toLearn.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toLearn.Model.Domain.Follow;
import com.toLearn.Model.Domain.User;
import com.toLearn.Model.VO.UserVO;

import java.util.List;

/**
 * 关注服务
 * @author BraumAce
 */
public interface FollowService extends IService<Follow> {

    /**
     * 关注用户
     *
     * @param followUserId 关注用户id
     * @param userId       用户id
     */
    void followUser(Long followUserId, Long userId);

    /**
     * 关注我的粉丝
     *
     * @param userId 用户id
     * @return {@link List}<{@link UserVO}>
     */
    List<UserVO> listFans(Long userId);

    /**
     * 我关注的人
     *
     * @param userId 用户id
     * @return {@link List}<{@link UserVO}>
     */
    List<UserVO> listMyFollow(Long userId);

    /**
     * 分页我关注的人
     *
     * @param userId      用户id
     * @param currentPage 当前页码
     * @return {@link Page}<{@link UserVO}>
     */
    Page<UserVO> pageMyFollow(Long userId, String currentPage);

    /**
     * 分页我的粉丝
     *
     * @param userId      用户id
     * @param currentPage 当前页码
     * @return {@link Page}<{@link UserVO}>
     */
    Page<UserVO> pageFans(Long userId, String currentPage);

    /**
     * 获取用户关注信息
     *
     * @param user   用户
     * @param userId 用户id
     * @return {@link UserVO}
     */
    UserVO getUserFollowInfo(User user, long userId);

}
