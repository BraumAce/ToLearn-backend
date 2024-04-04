package com.toLearn.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toLearn.Model.Domain.Friends;
import org.apache.ibatis.annotations.Mapper;

/**
* @author BraumAce
* @description 针对表【friends(好友申请管理表)】的数据库操作Mapper
*/
@Mapper
public interface FriendsMapper extends BaseMapper<Friends> {

}




