package com.tolearn.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tolearn.Model.Domain.Friends;
import com.tolearn.Model.Domain.User;
import com.tolearn.Model.Request.FriendAddRequest;
import com.tolearn.Model.VO.FriendsRecordVO;

import java.util.List;
import java.util.Set;

/**
 * 好友服务
 */
public interface FriendsService extends IService<Friends> {

    /**
     * 好友申请
     *
     * @param loginUser        登录用户
     * @param friendAddRequest 好友添加请求
     * @return boolean
     */
    boolean addFriendRecords(User loginUser, FriendAddRequest friendAddRequest);

    /**
     * 查询出所有申请、同意记录
     *
     * @param loginUser 登录用户
     * @return {@link List}<{@link FriendsRecordVO}>
     */
    List<FriendsRecordVO> obtainFriendApplicationRecords(User loginUser);

    /**
     * 同意申请
     *
     * @param loginUser 登录用户
     * @param fromId    从…起id
     * @return boolean
     */
    boolean agreeToApply(User loginUser, Long fromId);

    /**
     * 撤销好友申请
     *
     * @param id        申请记录id
     * @param loginUser 登录用户
     * @return boolean
     */
    boolean canceledApply(Long id, User loginUser);

    /**
     * 获取我申请的记录
     *
     * @param loginUser 登录用户
     * @return {@link List}<{@link FriendsRecordVO}>
     */
    List<FriendsRecordVO> getMyRecords(User loginUser);

    /**
     * 获取未读记录条数
     *
     * @param loginUser 登录用户
     * @return int
     */
    int getRecordCount(User loginUser);

    /**
     * 读取纪录
     *
     * @param loginUser 登录用户
     * @param ids       ids
     * @return boolean
     */
    boolean toRead(User loginUser, Set<Long> ids);

}
