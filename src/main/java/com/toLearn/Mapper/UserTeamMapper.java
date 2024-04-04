package com.toLearn.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toLearn.Model.Domain.UserTeam;
import org.apache.ibatis.annotations.Mapper;

/**
* @author BraumAce
* @description 针对表【user_team(用户队伍关系)】的数据库操作Mapper
*/
@Mapper
public interface UserTeamMapper extends BaseMapper<UserTeam> {

}




