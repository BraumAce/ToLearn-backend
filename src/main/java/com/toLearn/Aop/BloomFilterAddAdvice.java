package com.toLearn.Aop;

import cn.hutool.bloomfilter.BloomFilter;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.toLearn.Constants.BloomFilterConstants.*;

/**
 * 布隆过滤器添加通知
 * @author BraumAce
 */
@Component
@Aspect
@ConditionalOnProperty(prefix = "tolearn", name = "enable-bloom-filter", havingValue = "true")
@Log4j2
public class BloomFilterAddAdvice {
    @Resource
    private BloomFilter bloomFilter;

    /**
     * 之后插入用户
     * @param joinPoint 连接点
     */
    @After("execution(* com.toLearn.Service.Impl.UserServiceImpl.afterInsertUser(..))")
    public void afterInsertUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("add userId " + args[1] + " to BloomFilter");
        bloomFilter.add(USER_BLOOM_PREFIX + args[1]);
    }

    /**
     * 添加队伍后
     * @param ret ret
     */
    @AfterReturning(value = "execution(long com.toLearn.Service.Impl.TeamServiceImpl.addTeam(..))", returning = "ret")
    public void afterAddTeam(Object ret) {
        log.info("add teamId " + ret + " to BloomFilter");
        bloomFilter.add(TEAM_BLOOM_PREFIX + ret);
    }

    /**
     * 添加博客后
     * @param ret ret
     */
    @AfterReturning(value = "execution(* com.toLearn.Service.Impl.BlogServiceImpl.addBlog(..))", returning = "ret")
    public void afterAddBlog(Object ret) {
        log.info("add blogId " + ret + " to BloomFilter");
        bloomFilter.add(BLOG_BLOOM_PREFIX + ret);
    }
}
