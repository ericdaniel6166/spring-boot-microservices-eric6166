package com.eric6166.aws.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@ConditionalOnProperty(name = "cloud.aws.s3.enabled", havingValue = "true")
public class S3Config {

    AWSProps awsProps;

    @Bean
    public S3Client s3Client(AwsCredentialsProvider credentialsProvider) {
        return S3Client.builder()
                .region(Region.of(awsProps.getRegion()))
                .credentialsProvider(credentialsProvider)
                .build();
    }

}
