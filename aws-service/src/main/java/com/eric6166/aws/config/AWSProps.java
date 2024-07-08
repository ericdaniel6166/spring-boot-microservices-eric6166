package com.eric6166.aws.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "cloud.aws.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "cloud.aws")
public class AWSProps {

    private Credentials credentials;
    private String region;

    @Getter
    @Setter
    public static class Credentials {
        private String accessKey; // this is for local testing, pls use AWS Secret Manager
        private String secretKey; // this is for local testing, pls use AWS Secret Manager
    }
}
