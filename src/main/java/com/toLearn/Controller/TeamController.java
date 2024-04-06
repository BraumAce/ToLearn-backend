package com.toLearn.Controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toLearn.Common.BaseResponse;
import com.toLearn.Common.ErrorCode;
import com.toLearn.Common.ResultUtils;
import com.toLearn.Exception.BusinessException;
import com.toLearn.Model.Domain.Team;
import com.toLearn.Model.Domain.User;
import com.toLearn.Model.Domain.UserTeam;
import com.toLearn.Model.Request.*;
import com.toLearn.Model.VO.TeamVO;
import com.toLearn.Model.VO.UserVO;
import com.toLearn.Service.TeamService;
import com.toLearn.Service.UserService;
import com.toLearn.Service.UserTeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 队伍控制器
 * @author BraumAce
 */
@RestController
@RequestMapping("/team")
@Api(tags = "队伍管理模块")
@Log4j2
public class TeamController {
    // 队伍服务
    @Resource
    private TeamService teamService;

    // 用户服务
    @Resource
    private UserService userService;

    // 用户队伍服务
    @Resource
    private UserTeamService userTeamService;

    /**
     * 添加队伍
     * @param teamAddRequest 队伍添加请求
     * @param request        请求
     * @return {@link BaseResponse}<{@link Long}>
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamAddRequest", value = "队伍添加请求参数"),
                        @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Team team = new Team();
        BeanUtil.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    /**
     * 更新队伍
     * @param teamUpdateRequest 队伍更新请求
     * @param request           请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamUpdateRequest", value = "队伍更新请求参数"),
                        @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest,
                                            HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean result = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }


    /**
     * 根据id查询队伍
     * @param id      队伍id
     * @param request 请求
     * @return {@link BaseResponse}<{@link TeamVO}>
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "队伍id")})
    public BaseResponse<TeamVO> getTeamById(@PathVariable Long id, HttpServletRequest request) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return ResultUtils.success(teamService.getTeam(id, loginUser.getId()));
    }

    /**
     * 获取队伍列表
     * @param currentPage      当前页面
     * @param teamQueryRequest 队伍搜索请求
     * @param request          请求
     * @return {@link BaseResponse}<{@link Page}<{@link TeamVO}>>
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取队伍列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamQueryRequest", value = "队伍查询请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<TeamVO>> listTeams(long currentPage, TeamQueryRequest teamQueryRequest,
                                                HttpServletRequest request) {
        if (teamQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Page<TeamVO> teamVoPage = teamService.listTeams(currentPage, teamQueryRequest, userService.isAdmin(loginUser));
        Page<TeamVO> teamVoPageWithAvatar = teamService.getJoinedUserAvatar(teamVoPage);
        Page<TeamVO> finalPage = getTeamHasJoinNum(teamVoPageWithAvatar);
        return getUserJoinedList(loginUser, finalPage);
    }

    /**
     * 加入队伍
     * @param teamJoinRequest 队伍加入请求
     * @param request         请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/join")
    @ApiOperation(value = "加入队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamJoinRequest", value = "加入队伍请求参数"),
                        @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 退出队伍
     * @param teamQuitRequest 队伍退出请求
     * @param request         请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/quit")
    @ApiOperation(value = "退出队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamQuitRequest", value = "退出队伍请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 解散队伍
     * @param deleteRequest 删除请求
     * @param request       请求
     * @return {@link BaseResponse}<{@link Boolean}>
     */
    @PostMapping("/delete")
    @ApiOperation(value = "解散队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "deleteRequest", value = "解散队伍请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean isAdmin = userService.isAdmin(loginUser);
        boolean result = teamService.deleteTeam(id, loginUser, isAdmin);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 获取我创建的队伍
     * @param currentPage 当前页面
     * @param teamQuery   队伍查询
     * @param request     请求
     * @return {@link BaseResponse}<{@link Page}<{@link TeamVO}>>
     */
    @GetMapping("/list/my/create")
    @ApiOperation(value = "获取我创建的队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamQuery", value = "获取队伍请求参数"),
                        @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<TeamVO>> listMyCreateTeams(long currentPage,
                                                        TeamQueryRequest teamQuery,
                                                        HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        teamQuery.setUserId(loginUser.getId());
        Page<TeamVO> teamVOPage = teamService.listMyCreate(currentPage, loginUser.getId());
        Page<TeamVO> finalPage = getTeamHasJoinNum(teamVOPage);
        return getUserJoinedList(loginUser, finalPage);
    }

    /**
     * 获取我加入的队伍
     * @param currentPage 当前页面
     * @param teamQuery   队伍查询
     * @param request     请求
     * @return {@link BaseResponse}<{@link Page}<{@link TeamVO}>>
     */
    @GetMapping("/list/my/join")
    @ApiOperation(value = "获取我加入的队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamQuery", value = "获取队伍请求参数"),
            @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<Page<TeamVO>> listMyJoinTeams(long currentPage,
                                                      TeamQueryRequest teamQuery,
                                                      HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        LambdaQueryWrapper<UserTeam> userTeamLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userTeamLambdaQueryWrapper.eq(UserTeam::getUserId, loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(userTeamLambdaQueryWrapper);
        Map<Long, List<UserTeam>> listMap = userTeamList.stream()
                .collect(Collectors.groupingBy(UserTeam::getTeamId));
        List<Long> idList = new ArrayList<>(listMap.keySet());
        if (idList.isEmpty()) {
            return ResultUtils.success(new Page<>());
        }
        teamQuery.setIdList(idList);
        Page<TeamVO> teamVOPage = teamService.listMyJoin(currentPage, teamQuery);
        Page<TeamVO> finalPage = getTeamHasJoinNum(teamVOPage);
        return getUserJoinedList(loginUser, finalPage);
    }

    /**
     * 获取所有我加入的队伍
     * @param request 请求
     * @return {@link BaseResponse}<{@link List}<{@link TeamVO}>>
     */
    @GetMapping("/list/my/join/all")
    @ApiOperation(value = "获取所有我加入的队伍")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamQuery", value = "获取队伍请求参数"),
                        @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<TeamVO>> listAllMyJoinTeams(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        List<TeamVO> teamVOList = teamService.listAllMyJoin(loginUser.getId());
        return ResultUtils.success(teamVOList);
    }

    /**
     * 根据id获取队伍成员
     * @param id      队伍id
     * @param request 请求
     * @return {@link BaseResponse}<{@link List}<{@link UserVO}>>
     */
    @GetMapping("/member/{id}")
    @ApiOperation(value = "根据id获取队伍成员")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "队伍id"),
                        @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<List<UserVO>> getTeamMemberById(@PathVariable Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (id == null || id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<UserVO> teamMember = teamService.getTeamMember(id, loginUser.getId());
        return ResultUtils.success(teamMember);
    }

    /**
     * 更新队伍封面图片
     * @param teamCoverUpdateRequest 团队封面更新请求
     * @param request                请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PutMapping("/cover")
    @ApiOperation(value = "更新队伍封面图片")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamCoverUpdateRequest", value = "队伍封面更新请求"),
                        @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> changeCoverImage(TeamCoverUpdateRequest teamCoverUpdateRequest,
                                                 HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        boolean admin = userService.isAdmin(loginUser);
        teamService.changeCoverImage(teamCoverUpdateRequest, loginUser.getId(), admin);
        return ResultUtils.success("ok");
    }

    /**
     * 踢出队员
     * @param teamKickOutRequest 踢出队员请求
     * @param request            请求
     * @return {@link BaseResponse}<{@link String}>
     */
    @PostMapping("/kick")
    @ApiOperation(value = "踢出队员")
    @ApiImplicitParams({@ApiImplicitParam(name = "teamKickOutRequest", value = "踢出队员请求"),
                        @ApiImplicitParam(name = "request", value = "request请求")})
    public BaseResponse<String> kickOut(@RequestBody TeamKickOutRequest teamKickOutRequest,
                                        HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long teamId = teamKickOutRequest.getTeamId();
        Long userId = teamKickOutRequest.getUserId();
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean admin = userService.isAdmin(loginUser);
        teamService.kickOut(teamId, userId, loginUser.getId(), admin);
        return ResultUtils.success("ok");
    }

    /**
     * 获取已加入的用户
     * @param loginUser 登录用户
     * @param teamPage  队伍页面
     * @return {@link BaseResponse}<{@link Page}<{@link TeamVO}>>
     */
    @ApiOperation(value = "获取已加入的用户")
    private BaseResponse<Page<TeamVO>> getUserJoinedList(User loginUser, Page<TeamVO> teamPage) {
        try {
            List<TeamVO> teamList = teamPage.getRecords();
            List<Long> teamIdList = teamList.stream().map(TeamVO::getId).collect(Collectors.toList());
            //判断当前用户已加入的队伍
            LambdaQueryWrapper<UserTeam> userTeamLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userTeamLambdaQueryWrapper.eq(UserTeam::getUserId, loginUser.getId()).in(UserTeam::getTeamId, teamIdList);
            //用户已加入的队伍
            List<UserTeam> userTeamList = userTeamService.list(userTeamLambdaQueryWrapper);
            Set<Long> joinedTeamIdList = userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
            teamList.forEach(team -> team.setHasJoin(joinedTeamIdList.contains(team.getId())));
            teamPage.setRecords(teamList);
        } catch (Exception ignored) {
        }
        return ResultUtils.success(teamPage);
    }

    /**
     * 获取队伍已加入人数
     * @param teamVOPage teamVoPage
     * @return {@link Page}<{@link TeamVO}>
     */
    @ApiOperation(value = "获取队伍已加入人数")
    private Page<TeamVO> getTeamHasJoinNum(Page<TeamVO> teamVOPage) {
        List<TeamVO> teamList = teamVOPage.getRecords();
        teamList.forEach((team) -> {
            LambdaQueryWrapper<UserTeam> userTeamLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userTeamLambdaQueryWrapper.eq(UserTeam::getTeamId, team.getId());
            long hasJoinNum = userTeamService.count(userTeamLambdaQueryWrapper);
            team.setHasJoinNum(hasJoinNum);
        });
        teamVOPage.setRecords(teamList);
        return teamVOPage;
    }
}
