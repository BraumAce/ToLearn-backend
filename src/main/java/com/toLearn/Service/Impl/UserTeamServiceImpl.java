package com.toLearn.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toLearn.Mapper.UserTeamMapper;
import com.toLearn.Model.Domain.UserTeam;
import com.toLearn.Service.UserTeamService;
import org.springframework.stereotype.Service;

/**
 * 用户队伍服务实现
 * @author BraumAce
 */
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {

}




