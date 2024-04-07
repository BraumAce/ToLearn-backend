package com.toLearn.Aop;

import cn.hutool.bloomfilter.BloomFilter;
import com.toLearn.Common.ErrorCode;
import com.toLearn.Exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.toLearn.Constants.BloomFilterConstants.*;


/**
 * 布隆过滤器搜索通知
 * @author BraumAce
 */
@Component
@Aspect
@ConditionalOnProperty(prefix = "tolearn", name = "enable-bloom-filter", havingValue = "true")
@Log4j2
public class BloomFilterSearchAdvice {
    @Resource
    private BloomFilter bloomFilter;

    /**
     * 通过id查找用户
     * @param joinPoint 连接点
     */
    @Before("execution(* com.toLearn.Controller.UserController.getUserById(..))")
    public void findUserById(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        boolean contains = bloomFilter.contains(USER_BLOOM_PREFIX + args[0]);
        if (!contains) {
            log.error("没有在 BloomFilter 中找到该 userId");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有找到该用户");
        }
    }

    /**
     * 通过id查找队伍
     * @param joinPoint 连接点
     */
    @Before("execution(* com.toLearn.Controller.TeamController.getTeamById(..))")
    public void findTeamById(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        boolean contains = bloomFilter.contains(TEAM_BLOOM_PREFIX + args[0]);
        if (!contains) {
            log.error("没有在 BloomFilter 中找到该 teamId");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有找到该队伍");
        }
    }

    /**
     * 通过id查找队伍成员
     * @param joinPoint 连接点
     */
    @Before("execution(* com.toLearn.Controller.TeamController.getTeamMemberById(..))")
    public void findTeamMemberById(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        boolean contains = bloomFilter.contains(TEAM_BLOOM_PREFIX + args[0]);
        if (!contains) {
            log.error("没有在 BloomFilter 中找到该 teamId");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有找到该队伍");
        }
    }

    /**
     * 通过id查找博客
     * @param joinPoint 连接点
     */
    @Before("execution(* com.toLearn.Controller.BlogController.getBlogById(..))")
    public void findBlogById(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        boolean contains = bloomFilter.contains(BLOG_BLOOM_PREFIX + args[0]);
        if (!contains) {
            log.error("没有在 BloomFilter 中找到该 blogId");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有找到该博文");
        }
    }
}
