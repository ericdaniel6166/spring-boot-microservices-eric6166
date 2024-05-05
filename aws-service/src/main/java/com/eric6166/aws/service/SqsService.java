package com.eric6166.aws.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.DeleteQueueResponse;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import java.util.Map;

@ConditionalOnProperty(name = "cloud.aws.sqs.enabled", havingValue = "true")
public interface SqsService {

    CreateQueueResponse createQueue(String queueName, Map<QueueAttributeName, String> attributes);

    GetQueueUrlResponse getQueueUrl(String queueName);

    DeleteQueueResponse deleteQueue(String queueName);
}
