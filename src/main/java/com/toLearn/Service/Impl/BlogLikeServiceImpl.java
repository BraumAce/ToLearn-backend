package com.toLearn.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toLearn.Mapper.BlogLikeMapper;
import com.toLearn.Model.Domain.BlogLike;
import com.toLearn.Service.BlogLikeService;
import org.springframework.stereotype.Service;

/**
 * 博文点赞服务实现
 * @author BraumAce
 */
@Service
public class BlogLikeServiceImpl extends ServiceImpl<BlogLikeMapper, BlogLike>
        implements BlogLikeService {

}




