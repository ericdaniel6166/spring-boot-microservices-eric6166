package com.eric6166.aws.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "cloud.aws.enabled", havingValue = "true")
public class AWSConfig {

    private final AWSProps awsProps;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        var credentials = awsProps.getCredentials();
        if (credentials != null && StringUtils.isNotBlank(credentials.getAccessKey())) {
            return () -> AwsBasicCredentials.create(
                    credentials.getAccessKey(), // this is for local testing, pls use AWS Secret Manager
                    credentials.getSecretKey()); // this is for local testing, pls use AWS Secret Manager
        } else {
            return DefaultCredentialsProvider.create();
        }
    }


}
