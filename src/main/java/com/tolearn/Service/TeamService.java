package com.tolearn.Service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tolearn.Model.Domain.Team;
import com.tolearn.Model.Domain.User;
import com.tolearn.Model.Request.*;
import com.tolearn.Model.VO.TeamVO;
import com.tolearn.Model.VO.UserVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 队伍服务
 */
public interface TeamService extends IService<Team> {

    /**
     * 添加队伍
     *
     * @param team      队伍
     * @param loginUser 登录用户
     * @return long
     */
    @Transactional(rollbackFor = Exception.class)
    long addTeam(Team team, User loginUser);

    /**
     * 列出队伍
     *
     * @param currentPage 当前页码
     * @param teamQuery   队伍查询
     * @param isAdmin     是否为管理员
     * @return {@link Page}<{@link TeamVO}>
     */
    Page<TeamVO> listTeams(long currentPage, TeamQueryRequest teamQuery, boolean isAdmin);

    /**
     * 更新队伍
     *
     * @param teamUpdateRequest 队伍更新请求
     * @param loginUser         登录用户
     * @return boolean
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     *
     * @param teamJoinRequest 队伍加入请求
     * @param loginUser       登录用户
     * @return boolean
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     *
     * @param teamQuitRequest 队伍退出请求
     * @param loginUser       登录用户
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除队伍
     *
     * @param id        id
     * @param loginUser 登录用户
     * @param isAdmin   是否为管理员
     * @return boolean
     */
    boolean deleteTeam(long id, User loginUser, boolean isAdmin);

    /**
     * 获取队伍
     *
     * @param teamId 队伍id
     * @param userId 用户id
     * @return {@link TeamVO}
     */
    TeamVO getTeam(Long teamId, Long userId);

    /**
     * 列出我加入的队伍
     *
     * @param currentPage 当前页码
     * @param teamQuery   队伍查询
     * @return {@link Page}<{@link TeamVO}>
     */
    Page<TeamVO> listMyJoin(long currentPage, TeamQueryRequest teamQuery);

    /**
     * 获取队伍成员
     *
     * @param teamId 队伍id
     * @param userId 用户id
     * @return {@link List}<{@link UserVO}>
     */
    List<UserVO> getTeamMember(Long teamId, Long userId);

    /**
     * 列出所有我加入的队伍
     *
     * @param id id
     * @return {@link List}<{@link TeamVO}>
     */
    List<TeamVO> listAllMyJoin(Long id);

    /**
     * 更改封面图像
     *
     * @param request 要求
     * @param userId  用户id
     * @param admin   管理
     */
    void changeCoverImage(TeamCoverUpdateRequest request, Long userId, boolean admin);

    /**
     * 踢出
     *
     * @param teamId      队伍id
     * @param userId      用户id
     * @param loginUserId 登录用户id
     * @param admin       管理
     */
    void kickOut(Long teamId, Long userId, Long loginUserId, boolean admin);

    /**
     * 列出我创建的队伍
     *
     * @param currentPage 当前页码
     * @param userId      用户id
     * @return {@link Page}<{@link TeamVO}>
     */
    Page<TeamVO> listMyCreate(long currentPage, Long userId);

    /**
     * 获取已加入队员头像
     *
     * @param teamVoPage 队伍vo分页
     * @return {@link Page}<{@link TeamVO}>
     */
    Page<TeamVO> getJoinedUserAvatar(Page<TeamVO> teamVoPage);

}
