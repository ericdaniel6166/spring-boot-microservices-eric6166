package com.eric6166.aws.config;

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
@ConditionalOnProperty(name = "cloud.aws.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "cloud.aws")
public class AWSProps {

    Credentials credentials;
    String region;

    @Getter
    @Setter
    public static class Credentials {
        String accessKey;
        String secretKey;
    }
}
