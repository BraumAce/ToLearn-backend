package com.tolearn.Properties;

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
public class tolearnProperties {


}
