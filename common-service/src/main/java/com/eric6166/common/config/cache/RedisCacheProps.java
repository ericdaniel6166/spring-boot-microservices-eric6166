package com.eric6166.common.config.cache;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisCacheProps {
    Long timeToLive;

}
