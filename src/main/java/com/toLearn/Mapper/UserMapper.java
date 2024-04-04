package com.toLearn.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toLearn.Model.Domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author BraumAce
 * @description 针对表【user】的数据库操作Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 获取随机用户
    List<User> getRandomUser();
}




