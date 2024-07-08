package com.eric6166.aws.sqs;

import com.eric6166.aws.config.AWSProps;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "cloud.aws.sqs.enabled", havingValue = "true")
public class SqsConfig {

    private final AWSProps awsProps;

    @Bean
    public SqsClient sqsClient(AwsCredentialsProvider credentialsProvider) {
        return SqsClient.builder()
                .region(Region.of(awsProps.getRegion()))
                .credentialsProvider(credentialsProvider)
                .build();
    }

}
