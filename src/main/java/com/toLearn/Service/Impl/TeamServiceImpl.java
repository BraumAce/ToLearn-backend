package com.toLearn.Service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toLearn.Common.ErrorCode;
import com.toLearn.Exception.BusinessException;
import com.toLearn.Mapper.TeamMapper;
import com.toLearn.Model.Domain.Team;
import com.toLearn.Model.Domain.User;
import com.toLearn.Model.Domain.UserTeam;
import com.toLearn.Model.Enums.TeamStatusEnum;
import com.toLearn.Model.Request.*;
import com.toLearn.Model.VO.TeamVO;
import com.toLearn.Model.VO.UserVO;
import com.toLearn.Properties.ToLearnProperties;
import com.toLearn.Service.FollowService;
import com.toLearn.Service.TeamService;
import com.toLearn.Service.UserService;
import com.toLearn.Service.UserTeamService;
import com.toLearn.Utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.toLearn.Constants.SystemConstants.PAGE_SIZE;
import static com.toLearn.Constants.TeamConstants.*;

/**
 * 队伍服务实现
 * @author BraumAce
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;

    @Resource
    private FollowService followService;

    @Value("${tolearn.qiniu.url:null}")
    private String qiniuUrl;

    @Resource
    private ToLearnProperties toLearnProperties;

    @Value("${server.servlet.session.cookie.domain}")
    private String host;

    @Value("${server.port}")
    private String port;

    /**
     * 添加队伍
     * @param team      队伍
     * @param loginUser 登录用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
        // todo redisson锁
        // 1. 请求参数是否为空？
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        if (team.getExpireTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(team.getExpireTime());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            team.setExpireTime(calendar.getTime());
        } else {
            team.setExpireTime(null);
        }
        final long userId = loginUser.getId();
        // 校验信息
        validateTeamParam(userId, team);
        // 8. 插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        // 9. 插入用户  => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        return teamId;
    }

    /**
     * 验证队伍参数
     * @param userId 用户id
     * @param team   队伍
     */
    public void validateTeamParam(Long userId, Team team) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        long hasTeamNum = this.count(queryWrapper);
        if (hasTeamNum >= MAXIMUM_TEAM_NUM) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户最多创建 5 个队伍");
        }
        //   1. 队伍人数 > 1 且 <= 20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > MAXIMUM_MEMBER_NUM) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        //   2. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > MAXIMUM_TITLE_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        //   3. 描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > MAXIMUM_DESCRIPTION_LEN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述过长");
        }
        //   4. status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        //   5. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > MAXIMUM_PASSWORD_LEN) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
            }
        }
        // 6. 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if (expireTime != null && new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间 > 当前时间");
        }
    }

    /**
     * 列出队伍
     * @param currentPage 当前页面
     * @param teamQuery   队伍查询
     * @param isAdmin     是管理
     */
    @Override
    public Page<TeamVO> listTeams(long currentPage, TeamQueryRequest teamQuery, boolean isAdmin) {
        LambdaQueryWrapper<Team> teamLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 组合查询条件
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            List<Long> idList = teamQuery.getIdList();
            String searchText = teamQuery.getSearchText();
            // 根据队伍名查询
            String name = teamQuery.getName();
            // 根据描述查询
            String description = teamQuery.getDescription();
            // 根绝最大人数查询
            Integer maxNum = teamQuery.getMaxNum();
            // 根据队长Id查询
            Long userId = teamQuery.getUserId();
            // 根据状态来查询
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
            if (statusEnum == null) {
                statusEnum = TeamStatusEnum.PUBLIC;
            }
            if (!isAdmin && statusEnum.equals(TeamStatusEnum.PRIVATE)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            teamLambdaQueryWrapper
                    .eq(id != null && id > 0, Team::getId, id)
                    .in(CollectionUtils.isNotEmpty(idList), Team::getId, idList)
                    .and(StringUtils.isNotBlank(searchText),
                            qw -> qw.like(Team::getName, searchText)
                                    .or()
                                    .like(Team::getDescription, searchText)
                    )
                    .like(StringUtils.isNotBlank(name), Team::getName, name)
                    .like(StringUtils.isNotBlank(description), Team::getDescription, description)
                    .eq(maxNum != null && maxNum > 0, Team::getMaxNum, maxNum)
                    .eq(userId != null && userId > 0, Team::getUserId, userId)
                    .eq(Team::getStatus, statusEnum.getValue())
                    .orderBy(true, false, Team::getCreateTime);
        }
        // 不展示已过期的队伍
        teamLambdaQueryWrapper.and(qw -> qw.gt(Team::getExpireTime, new Date()).or().isNull(Team::getExpireTime));
        return listTeamByCondition(currentPage, teamLambdaQueryWrapper);
    }

    /**
     * 更新队伍
     * @param teamUpdateRequest 队伍更新请求
     * @param loginUser         登录用户
     */
    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        // 只有管理员或者队伍的创建者可以修改
        if (!oldTeam.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        if (statusEnum.equals(TeamStatusEnum.SECRET)) {
            if (StringUtils.isBlank(teamUpdateRequest.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "加密房间必须要设置密码");
            }
        }
        Team updateTeam = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, updateTeam);
        return this.updateById(updateTeam);
    }

    /**
     * 加入队伍
     * @param teamJoinRequest 队伍加入请求
     * @param loginUser       登录用户
     */
    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamJoinRequest.getTeamId();
        Team team = getTeamById(teamId);
        Date expireTime = team.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        Integer status = team.getStatus();
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(teamStatusEnum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "禁止加入私有队伍");
        }
        String password = teamJoinRequest.getPassword();
        if (TeamStatusEnum.SECRET.equals(teamStatusEnum)) {
            if (StringUtils.isBlank(password) || !password.equals(team.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
            }
        }
        // 该用户已加入的队伍数量
        long userId = loginUser.getId();
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("user_id", userId);
        long hasJoinNum = userTeamService.count(userTeamQueryWrapper);
        if (hasJoinNum > MAXIMUM_JOINED_TEAM) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多创建和加入 5 个队伍");
        }
        // 不能重复加入已加入的队伍
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("user_id", userId);
        userTeamQueryWrapper.eq("team_id", teamId);
        long hasUserJoinTeam = userTeamService.count(userTeamQueryWrapper);
        if (hasUserJoinTeam > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已加入该队伍");
        }
        // 已加入队伍的人数
        long teamHasJoinNum = this.countTeamUserByTeamId(teamId);
        if (teamHasJoinNum >= team.getMaxNum()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已满");
        }
        // 修改队伍信息
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);
    }

    /**
     * 退出队伍
     * @param teamQuitRequest 队伍辞职请求
     * @param loginUser       登录用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamQuitRequest.getTeamId();
        Team team = getTeamById(teamId);
        long userId = loginUser.getId();
        UserTeam queryUserTeam = new UserTeam();
        queryUserTeam.setTeamId(teamId);
        queryUserTeam.setUserId(userId);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>(queryUserTeam);
        long count = userTeamService.count(queryWrapper);
        if (count == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未加入队伍");
        }
        long teamHasJoinNum = this.countTeamUserByTeamId(teamId);
        // 队伍只剩一人，解散
        if (teamHasJoinNum == 1) {
            // 删除队伍
            this.removeById(teamId);
        } else {
            // 队伍还剩至少两人
            // 是队长
            if (team.getUserId() == userId) {
                // 把队伍转移给最早加入的用户
                // 1. 查询已加入队伍的所有用户和加入时间
                QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
                userTeamQueryWrapper.eq("team_id", teamId);
                userTeamQueryWrapper.last("order by id asc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
                if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextTeamLeaderId = nextUserTeam.getUserId();
                // 更新当前队伍的队长
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextTeamLeaderId);
                boolean result = this.updateById(updateTeam);
                if (!result) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍队长失败");
                }
            }
        }
        // 移除关系
        return userTeamService.remove(queryWrapper);
    }

    /**
     * 删除队伍
     * @param id        id
     * @param loginUser 登录用户
     * @param isAdmin   是否为管理员
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeam(long id, User loginUser, boolean isAdmin) {
        // 校验队伍是否存在
        Team team = getTeamById(id);
        long teamId = team.getId();
        // 校验你是不是队伍的队长
        if (isAdmin) {
            // 移除所有加入队伍的关联信息
            QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
            userTeamQueryWrapper.eq("team_id", teamId);
            boolean result = userTeamService.remove(userTeamQueryWrapper);
            if (!result) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍关联信息失败");
            }
            return this.removeById(teamId);
        }
        if (!team.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无访问权限");
        }
        // 移除所有加入队伍的关联信息
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("team_id", teamId);
        boolean result = userTeamService.remove(userTeamQueryWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍关联信息失败");
        }
        // 删除队伍
        return this.removeById(teamId);
    }

    /**
     * 得到队伍
     * @param teamId 队伍id
     * @param userId 用户id
     */
    @Override
    public TeamVO getTeam(Long teamId, Long userId) {
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在");
        }
        TeamVO teamVO = new TeamVO();
        BeanUtils.copyProperties(team, teamVO);
        LambdaQueryWrapper<UserTeam> userTeamLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userTeamLambdaQueryWrapper.eq(UserTeam::getTeamId, teamId);
        long count = userTeamService.count(userTeamLambdaQueryWrapper);
        teamVO.setHasJoinNum(count);
        userTeamLambdaQueryWrapper.eq(UserTeam::getUserId, userId);
        long userJoin = userTeamService.count(userTeamLambdaQueryWrapper);
        teamVO.setHasJoin(userJoin > 0);
        User leader = userService.getById(team.getUserId());
        teamVO.setLeaderName(leader.getUsername());

        return teamVO;
    }

    /**
     * 列出我加入的队伍
     *
     * @param currentPage 当前页面
     * @param teamQuery   队伍查询
     * @return {@link Page}<{@link TeamVO}>
     */
    @Override
    public Page<TeamVO> listMyJoin(long currentPage, TeamQueryRequest teamQuery) {
        List<Long> idList = teamQuery.getIdList();
        LambdaQueryWrapper<Team> teamLambdaQueryWrapper = new LambdaQueryWrapper<>();
        teamLambdaQueryWrapper.in(Team::getId, idList);
        return listTeamByCondition(currentPage, teamLambdaQueryWrapper);
    }

    /**
     * 按条件列出队伍
     * @param currentPage            当前页码
     * @param teamLambdaQueryWrapper 队伍lambda查询包装器
     */
    public Page<TeamVO> listTeamByCondition(long currentPage, LambdaQueryWrapper<Team> teamLambdaQueryWrapper) {
        Page<Team> teamPage = this.page(new Page<>(currentPage, PAGE_SIZE), teamLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(teamPage.getRecords())) {
            return new Page<>();
        }
        Page<TeamVO> teamVoPage = new Page<>();
        // 关联查询创建人的用户信息
        BeanUtils.copyProperties(teamPage, teamVoPage, "records");
        List<Team> teamPageRecords = teamPage.getRecords();
        ArrayList<TeamVO> teamUserVOList = new ArrayList<>();
        for (Team team : teamPageRecords) {
            Long userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            User user = userService.getById(userId);
            TeamVO teamUserVO = new TeamVO();
            BeanUtils.copyProperties(team, teamUserVO);
            // 脱敏用户信息
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                teamUserVO.setCreateUser(userVO);
            }
            teamUserVOList.add(teamUserVO);
        }
        teamVoPage.setRecords(teamUserVOList);
        return teamVoPage;
    }

    /**
     * 获取队伍成员
     * @param teamId 队伍id
     * @param userId 用户id
     */
    @Override
    public List<UserVO> getTeamMember(Long teamId, Long userId) {
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在");
        }
        LambdaQueryWrapper<UserTeam> userTeamLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userTeamLambdaQueryWrapper.eq(UserTeam::getTeamId, teamId);
        List<UserTeam> userTeamList = userTeamService.list(userTeamLambdaQueryWrapper);
        List<Long> userIdList = userTeamList.stream()
                .map(UserTeam::getUserId)
                .filter(id -> !Objects.equals(id, userId))
                .collect(Collectors.toList());
        if (userIdList.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.in(User::getId, userIdList);
        List<User> userList = userService.list(userLambdaQueryWrapper);
        return userList.stream()
                .map((user) -> followService.getUserFollowInfo(user, userId))
                .collect(Collectors.toList());
    }

    /**
     * 列出所有我加入的队伍
     * @param id id
     */
    @Override
    public List<TeamVO> listAllMyJoin(Long id) {
        LambdaQueryWrapper<UserTeam> userTeamLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userTeamLambdaQueryWrapper.eq(UserTeam::getUserId, id);
        List<Long> teamIds = userTeamService.list(userTeamLambdaQueryWrapper)
                .stream().map(UserTeam::getTeamId)
                .collect(Collectors.toList());
        if (teamIds.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Team> teamLambdaQueryWrapper = new LambdaQueryWrapper<>();
        teamLambdaQueryWrapper.in(Team::getId, teamIds);
        List<Team> teamList = this.list(teamLambdaQueryWrapper);
        return teamList.stream().map((team) -> {
            TeamVO teamVO = new TeamVO();
            BeanUtils.copyProperties(team, teamVO);
            teamVO.setHasJoin(true);
            return teamVO;
        }).collect(Collectors.toList());
    }


    /**
     * 更改封面图片
     * @param request 请求
     * @param userId  用户id
     * @param admin   管理
     */
    @Override
    public void changeCoverImage(TeamCoverUpdateRequest request, Long userId, boolean admin) {
        MultipartFile image = request.getFile();
        if (image == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = request.getId();
        if (teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!team.getUserId().equals(userId) && !admin) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (toLearnProperties.isUseLocalStorage()) {
            String fileName = FileUtils.uploadFile2Local(image);
            String fileUrl = "http://" + host + ":" + port + "/api/common/image/" + fileName;
            Team temp = new Team();
            temp.setId(team.getId());
            temp.setCoverImage(fileUrl);
            this.updateById(temp);
        } else {
            String fileName = FileUtils.uploadFile2Cloud(image);
            Team temp = new Team();
            temp.setId(team.getId());
            temp.setCoverImage(qiniuUrl + fileName);
            this.updateById(temp);
        }
    }

    /**
     * 踢出
     * @param teamId      队伍id
     * @param userId      用户id
     * @param loginUserId 登录用户id
     * @param admin       管理
     */
    @Override
    public void kickOut(Long teamId, Long userId, Long loginUserId, boolean admin) {
        if (userId.equals(loginUserId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能将自己踢出");
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍不存在");
        }
        if (!team.getUserId().equals(loginUserId) && !admin) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        LambdaQueryWrapper<UserTeam> userTeamLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userTeamLambdaQueryWrapper.eq(UserTeam::getTeamId, teamId).eq(UserTeam::getUserId, userId);
        userTeamService.remove(userTeamLambdaQueryWrapper);
    }

    /**
     * 列出我创建的队伍
     * @param currentPage 当前页码
     * @param userId      用户id
     */
    @Override
    public Page<TeamVO> listMyCreate(long currentPage, Long userId) {
        LambdaQueryWrapper<Team> teamLambdaQueryWrapper = new LambdaQueryWrapper<>();
        teamLambdaQueryWrapper.eq(Team::getUserId, userId);
        Page<Team> teamPage = this.page(new Page<>(currentPage, PAGE_SIZE), teamLambdaQueryWrapper);
        List<TeamVO> teamVOList = teamPage.getRecords()
                .stream().map((team) -> this.getTeam(team.getId(), userId))
                .collect(Collectors.toList());
        Page<TeamVO> teamVOPage = new Page<>();
        BeanUtils.copyProperties(teamPage, teamVOPage);
        teamVOPage.setRecords(teamVOList);
        return teamVOPage;
    }

    /**
     * 获取已加入队员头像
     * @param teamVoPage 队伍vo分页
     * @return
     */
    @Override
    public Page<TeamVO> getJoinedUserAvatar(Page<TeamVO> teamVoPage) {
        teamVoPage.getRecords().forEach((item) -> {
            Long teamId = item.getId();
            LambdaQueryWrapper<UserTeam> userTeamLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userTeamLambdaQueryWrapper.eq(UserTeam::getTeamId, teamId);
            List<Long> joinedUserIdList = userTeamService.list(userTeamLambdaQueryWrapper)
                    .stream().map((UserTeam::getUserId))
                    .limit(MAXIMUM_JOINED_USER_AVATAR_NUM)
                    .collect(Collectors.toList());
            List<String> joinedUserAvatarList = userService.listByIds(joinedUserIdList)
                    .stream().map((User::getAvatarUrl))
                    .collect(Collectors.toList());
            item.setJoinedUserAvatars(joinedUserAvatarList);
        });
        return teamVoPage;
    }


    /**
     * 根据id获取队伍信息
     * @param teamId 队伍id
     */
    private Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        return team;
    }

    /**
     * 根据队伍id获取队伍人数
     * @param teamId 队伍id
     */
    private long countTeamUserByTeamId(long teamId) {
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("team_id", teamId);
        return userTeamService.count(userTeamQueryWrapper);
    }

}




