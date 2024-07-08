package com.eric6166.common.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "spring.data.redis.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProps {
    private String hostname;
    private Integer port;

}
