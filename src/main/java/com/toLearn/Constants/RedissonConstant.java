package com.toLearn.Constants;

/**
 * Redisson常量
 * @author BraumAce
 */
public final class RedissonConstant {
    private RedissonConstant() {
    }

    // 应用锁
    public static final String APPLY_LOCK = "tolearn:apply:lock:";

    // 解散过期团队锁
    public static final String DISBAND_EXPIRED_TEAM_LOCK = "tolearn:disbandTeam:lock";

    // 用户推荐锁
    public static final String USER_RECOMMEND_LOCK = "tolearn:user:recommend:lock";

    // 博客点赞锁
    public static final String BLOG_LIKE_LOCK = "tolearn:blog:like:lock:";

    // 评论点赞锁
    public static final String COMMENTS_LIKE_LOCK = "tolearn:comments:like:lock:";

    // 默认等待时间
    public static final long DEFAULT_WAIT_TIME = 0;

    // 违约租赁时间
    public static final long DEFAULT_LEASE_TIME = -1;

}
