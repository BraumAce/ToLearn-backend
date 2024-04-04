package com.toLearn.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toLearn.Model.Domain.Team;
import org.apache.ibatis.annotations.Mapper;

/**
* @author BraumAce
* @description 针对表【team(队伍)】的数据库操作Mapper
 */
@Mapper
public interface TeamMapper extends BaseMapper<Team> {

}




