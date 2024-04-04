package com.toLearn.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toLearn.Model.Domain.Follow;
import org.apache.ibatis.annotations.Mapper;

/**
* @author BraumAce
* @description 针对表【follow】的数据库操作Mapper
*/
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

}




