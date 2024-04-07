package com.toLearn.Config;

import com.toLearn.Job.DisbandExpiredTeam;
import com.toLearn.Job.UserRecommendationCache;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quartz配置
 * @author BraumAce
 */
@Configuration
public class QuartzConfig {

    /**
     * 工作触发时间
     */
    @Value("${tolearn.job}")
    private String jobTriggerTime;

    /**
     * 解散队伍到期工作细节
     * @return {@link JobDetail}
     */
    @Bean
    public JobDetail disbandExpireTeamJobDetail() {
        return JobBuilder.newJob(DisbandExpiredTeam.class).storeDurably().build();
    }

    /**
     * 解散队伍触发到期
     * @return {@link Trigger}
     */
    @Bean
    public Trigger disbandExpireTeamTrigger() {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobTriggerTime);
        return TriggerBuilder.newTrigger()
                .forJob(disbandExpireTeamJobDetail())
                .withSchedule(cronScheduleBuilder).build();
    }

    /**
     * 用户推荐缓存工作细节
     * @return {@link JobDetail}
     */
    @Bean
    public JobDetail userRecommendationCacheJobDetail() {
        return JobBuilder.newJob(UserRecommendationCache.class).storeDurably().build();
    }

    /**
     * 用户推荐缓存触发
     * @return {@link Trigger}
     */
    @Bean
    public Trigger userRecommendationCacheTrigger() {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobTriggerTime);
        return TriggerBuilder.newTrigger()
                .forJob(userRecommendationCacheJobDetail())
                .withSchedule(cronScheduleBuilder).build();
    }
}
