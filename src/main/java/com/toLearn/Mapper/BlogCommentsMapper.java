package com.toLearn.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toLearn.Model.Domain.BlogComments;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author BraumAce
 * @description 针对表【blog_comments】的数据库操作Mapper
 */
@Mapper
public interface BlogCommentsMapper extends BaseMapper<BlogComments> {

}




