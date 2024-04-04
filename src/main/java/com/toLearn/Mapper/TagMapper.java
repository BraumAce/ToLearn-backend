package com.toLearn.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toLearn.Model.Domain.Tag;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author BraumAce
 * @description 针对表【tag(标签)】的数据库操作Mapper
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}




