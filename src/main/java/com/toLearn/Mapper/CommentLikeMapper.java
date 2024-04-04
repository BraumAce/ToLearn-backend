package com.toLearn.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toLearn.Model.Domain.CommentLike;
import org.apache.ibatis.annotations.Mapper;

/**
* @author BraumAce
* @description 针对表【comment_like】的数据库操作Mapper
*/
@Mapper
public interface CommentLikeMapper extends BaseMapper<CommentLike> {

}




