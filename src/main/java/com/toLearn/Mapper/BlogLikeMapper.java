package com.toLearn.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toLearn.Model.Domain.BlogLike;
import org.apache.ibatis.annotations.Mapper;

/**
* @author BraumAce
* @description 针对表【blog_like】的数据库操作Mapper
*/
@Mapper
public interface BlogLikeMapper extends BaseMapper<BlogLike> {

}




