package com.eric6166.aws.sqs;


import com.eric6166.aws.utils.AWSExceptionUtils;
import com.eric6166.aws.utils.AwsConst;
import com.eric6166.base.exception.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchResponse;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageResponse;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteQueueResponse;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.SendMessageBatchResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ConditionalOnProperty(name = "cloud.aws.sqs.enabled", havingValue = "true")
public class AppSqsClient {

    SqsClient sqsClient;
    SqsProps sqsProps;

    private Collection<SendMessageBatchRequestEntry> buildSendMessageBatchRequestEntries(SqsSendMessageBatchRequestEntries entries,
                                                                                         Integer delaySeconds, String messageGroupId, boolean fifoQueue) {
        return entries.getSqsSendMessageBatchRequestEntries().stream().map(entry ->
                buildSendMessageBatchRequestEntry(entry, delaySeconds, messageGroupId, fifoQueue)).collect(Collectors.toList());
    }

    private SendMessageBatchRequestEntry buildSendMessageBatchRequestEntry(SqsSendMessageBatchRequestEntry entry,
                                                                           Integer delaySeconds, String messageGroupId, boolean fifoQueue) {
        Integer inputDelaySeconds;
        if (delaySeconds != null) {
            inputDelaySeconds = delaySeconds;
        } else {
            inputDelaySeconds = sqsProps.getTemplate().getDelaySeconds();
        }
        String inputMessageGroupId;
        if (!fifoQueue) {
            inputMessageGroupId = null;
        } else if (StringUtils.isNotBlank(messageGroupId)) {
            inputMessageGroupId = messageGroupId;
        } else {
            inputMessageGroupId = sqsProps.getTemplate().getQueue().getFifo().getMessageGroupId();
        }
        return SendMessageBatchRequestEntry.builder()
                .messageBody(entry.getMessageBody())
                .id(entry.getId())
                .delaySeconds(inputDelaySeconds)
                .messageGroupId(inputMessageGroupId)
                .build();
    }

    public SendMessageBatchResponse sendMessageBatchByQueueUrl(String queueUrl, Integer delaySeconds, String messageGroupId,
                                                               SqsSendMessageBatchRequestEntries entries) throws AppException {
        return sendMessageBatchByQueueUrlAndEntries(queueUrl, buildSendMessageBatchRequestEntries(entries, delaySeconds,
                messageGroupId, StringUtils.endsWith(queueUrl, AwsConst.SQS_SUFFIX_FIFO)));
    }

    public SendMessageBatchResponse sendMessageBatchByQueueName(String queueName, Integer delaySeconds, String messageGroupId,
                                                                SqsSendMessageBatchRequestEntries entries) throws AppException {
        return sendMessageBatchByQueueUrl(getQueueUrl(queueName).queueUrl(), delaySeconds, messageGroupId, entries);
    }

    public SendMessageBatchResponse sendMessageBatchByQueueUrlAndEntries(String queueUrl, Collection<SendMessageBatchRequestEntry> entries) throws AppException {
        try {
            return sqsClient.sendMessageBatch(SendMessageBatchRequest.builder()
                    .queueUrl(queueUrl)
                    .entries(entries)
                    .build());
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e);
        }
    }

    public SendMessageResponse sendMessageByQueueUrl(String queueUrl, String message, Integer delaySeconds, String messageGroupId) throws AppException {
        try {
            var inputDelaySeconds = delaySeconds == null ? sqsProps.getTemplate().getDelaySeconds() : delaySeconds;
            boolean fifoQueue = StringUtils.endsWith(queueUrl, AwsConst.SQS_SUFFIX_FIFO);
            String inputMessageGroupId;
            if (!fifoQueue) {
                inputMessageGroupId = null;
            } else if (StringUtils.isNotBlank(messageGroupId)) {
                inputMessageGroupId = messageGroupId;
            } else {
                inputMessageGroupId = sqsProps.getTemplate().getQueue().getFifo().getMessageGroupId();
            }
            return sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .delaySeconds(inputDelaySeconds)
                    .messageGroupId(inputMessageGroupId)
                    .build()
            );
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e);
        }
    }

    public SendMessageResponse sendMessageByQueueUrl(String queueUrl, String message, Integer delaySeconds) throws AppException {
        return sendMessageByQueueUrl(queueUrl, message, delaySeconds, null);
    }

    public SendMessageResponse sendMessageByQueueName(String queueName, String message, Integer delaySeconds, String messageGroupId) throws AppException {
        return sendMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), message, delaySeconds, messageGroupId);
    }

    public SendMessageResponse sendMessageByQueueName(String queueName, String message, Integer delaySeconds) throws AppException {
        return sendMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), message, delaySeconds);
    }

    public DeleteQueueResponse deleteQueueByQueueName(String queueName) throws AppException {
        return deleteQueueByQueueUrl(getQueueUrl(queueName).queueUrl());
    }

    public DeleteQueueResponse deleteQueueByQueueUrl(String queueUrl) throws AppException {
        try {
            return sqsClient.deleteQueue(DeleteQueueRequest.builder()
                    .queueUrl(queueUrl)
                    .build());

        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e);
        }
    }

    public GetQueueUrlResponse getQueueUrl(String queueName) throws AppException {
        try {
            return sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build());
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueName '%s'", queueName));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e);
        }
    }

    public CreateQueueResponse createQueue(String queueName) throws AppException {
        Map<QueueAttributeName, String> queueAttributes = new EnumMap<>(QueueAttributeName.class);
        if (StringUtils.endsWith(queueName, AwsConst.SQS_SUFFIX_FIFO)) {
            queueAttributes.put(QueueAttributeName.FIFO_QUEUE, Boolean.TRUE.toString());
            queueAttributes.put(QueueAttributeName.CONTENT_BASED_DEDUPLICATION, Boolean.TRUE.toString());
        }
        return createQueue(queueName, queueAttributes);
    }

    public CreateQueueResponse createQueue(String queueName, Map<QueueAttributeName, String> attributes) throws AppException {
        try {
            return sqsClient.createQueue(CreateQueueRequest.builder()
                    .queueName(queueName)
                    .attributes(attributes)
                    .build());
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e);
        }
    }

    public ReceiveMessageResponse receiveMessageByQueueUrl(String queueUrl) throws AppException {
        return receiveMessageByQueueUrl(queueUrl, null);
    }

    public ReceiveMessageResponse receiveMessageByQueueUrl(String queueUrl, Integer maxNumberOfMessages) throws AppException {
        try {
            var inputMaxNumberOfMessages = maxNumberOfMessages == null ? sqsProps.getTemplate().getMaxNumberOfMessages() : maxNumberOfMessages;
            return sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(inputMaxNumberOfMessages)
                    .build());
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e);
        }
    }

    public ReceiveMessageResponse receiveMessageByQueueName(String queueName) throws AppException {
        return receiveMessageByQueueName(queueName, null);
    }

    public ReceiveMessageResponse receiveMessageByQueueName(String queueName, Integer maxNumberOfMessages) throws AppException {
        return receiveMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), maxNumberOfMessages);
    }

    public DeleteMessageBatchResponse deleteMessageBatchByQueueName(String queueName, SqsDeleteMessageBatchRequestEntries messages) throws AppException {
        return deleteMessageBatchByQueueUrl(getQueueUrl(queueName).queueUrl(), messages);
    }

    private DeleteMessageBatchResponse deleteMessageBatchByQueueUrl(String queueUrl, SqsDeleteMessageBatchRequestEntries messages) throws AppException {
        return deleteMessageBatchByQueueUrlAndEntries(queueUrl, buildDeleteMessageBatchRequestEntries(messages));
    }

    private Collection<DeleteMessageBatchRequestEntry> buildDeleteMessageBatchRequestEntries(SqsDeleteMessageBatchRequestEntries messages) {
        return messages.getSqsDeleteMessageBatchRequestEntries().stream().map(this::buildDeleteMessageBatchRequestEntry).toList();
    }

    private DeleteMessageBatchRequestEntry buildDeleteMessageBatchRequestEntry(SqsDeleteMessageBatchRequestEntry message) {
        return DeleteMessageBatchRequestEntry.builder()
                .receiptHandle(message.getReceiptHandle())
                .id(message.getId())
                .build();
    }

    public DeleteMessageBatchResponse deleteMessageBatchByQueueUrlAndEntries(String queueUrl, Collection<DeleteMessageBatchRequestEntry> entries) throws AppException {
        try {
            return sqsClient.deleteMessageBatch(DeleteMessageBatchRequest.builder()
                    .queueUrl(queueUrl)
                    .entries(entries)
                    .build());
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e);
        }

    }

    public DeleteMessageResponse deleteMessageByQueueName(String queueName, String receiptHandle) throws AppException {
        return deleteMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), receiptHandle);
    }

    public DeleteMessageResponse deleteMessageByQueueUrl(String queueUrl, String receiptHandle) throws AppException {
        try {
            return sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(receiptHandle)
                    .build());
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e);
        }

    }

//    void test() {
//        var span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("test").start();
//        try (var ws = tracer.withSpanInScope(span)) {
//
//        } catch (RuntimeException e) {
//            log.info("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
//            span.error(e);
//            throw e;
//        } finally {
//            span.finish();
//        }
//    }


    //listQueues
    //deleteMessage

}
