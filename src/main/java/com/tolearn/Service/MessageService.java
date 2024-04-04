package com.tolearn.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tolearn.Model.Domain.Message;
import com.tolearn.Model.VO.BlogVO;
import com.tolearn.Model.VO.MessageVO;

import java.util.List;

/**
 * 消息服务
 */
public interface MessageService extends IService<Message> {

    /**
     * 获取消息编号
     *
     * @param userId 用户id
     * @return long
     */
    long getMessageNum(Long userId);

    /**
     * 获取点赞数
     *
     * @param userId 用户id
     * @return long
     */
    long getLikeNum(Long userId);

    /**
     * 获取点赞列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link MessageVO}>
     */
    List<MessageVO> getLike(Long userId);

    /**
     * 获取用户博客列表
     *
     * @param userId 用户id
     * @return {@link List}<{@link BlogVO}>
     */
    List<BlogVO> getUserBlog(Long userId);

    /**
     * 有新消息
     *
     * @param userId 用户id
     * @return {@link Boolean}
     */
    Boolean hasNewMessage(Long userId);

    /**
     * 分页点赞列表
     *
     * @param userId      用户id
     * @param currentPage 当前页码
     * @return {@link Page}<{@link MessageVO}>
     */
    Page<MessageVO> pageLike(Long userId, Long currentPage);

}
