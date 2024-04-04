package com.toLearn.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toLearn.Mapper.TagMapper;
import com.toLearn.Model.Domain.Tag;
import com.toLearn.Service.TagService;
import org.springframework.stereotype.Service;

/**
 * 标签服务实现
 * @author BraumAce
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}




