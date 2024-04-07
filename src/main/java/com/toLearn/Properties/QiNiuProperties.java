package com.toLearn.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 七牛云配置
 * @author BraumAce
 */
@Data
@ConfigurationProperties(prefix = "tolearn.qiniu")
@Component
public class QiNiuProperties {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String url;
}
