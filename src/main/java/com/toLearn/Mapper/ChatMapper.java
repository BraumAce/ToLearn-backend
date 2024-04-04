package com.toLearn.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toLearn.Model.Domain.Chat;
import org.apache.ibatis.annotations.Mapper;

/**
* @author BraumAce
* @description 针对表【chat(聊天消息表)】的数据库操作Mapper
*/
@Mapper
public interface ChatMapper extends BaseMapper<Chat> {

}




