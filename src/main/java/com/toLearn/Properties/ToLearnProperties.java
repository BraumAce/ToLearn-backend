package com.toLearn.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置
 * @author BraumAce
 */

@Data
@Component
@ConfigurationProperties(prefix = "tolearn")
public class ToLearnProperties {

    // 本地图片位置
    private String img = "/img";

    // 定时任务
    private String job = "0 0 0 * * ? *";

    // 启用布隆过滤器
    private boolean enableBloomFilter = false;

    // 使用真实短信服务
    private boolean useShortMessagingService = false;

    // 使用图片本地存储
    private boolean useLocalStorage = true;

}
