package com.eric6166.aws.sqs;


import com.eric6166.aws.utils.AWSExceptionUtils;
import com.eric6166.aws.utils.AwsConst;
import com.eric6166.base.exception.AppException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
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

    private Collection<SendMessageBatchRequestEntry> buildSendMessageBatchRequestEntries(@NotNull SqsSendMessageBatchRequestEntries entries,
                                                                                         @NotNull Integer delaySeconds, String messageGroupId, boolean fifoQueue) {
        return entries.getSqsSendMessageBatchRequestEntries().stream().map(entry ->
                buildSendMessageBatchRequestEntry(entry, delaySeconds, messageGroupId, fifoQueue)).toList();
    }

    private SendMessageBatchRequestEntry buildSendMessageBatchRequestEntry(@NotNull SqsSendMessageBatchRequestEntry entry,
                                                                           @NotNull Integer delaySeconds, String messageGroupId, boolean fifoQueue) {
        Integer inputDelaySeconds;
        inputDelaySeconds = delaySeconds;
        String inputMessageGroupId;
        if (!fifoQueue) {
            inputMessageGroupId = null;
        } else {
            Assertions.assertTrue(StringUtils.isNotBlank(messageGroupId), "messageGroupId must not be blank");
            inputMessageGroupId = messageGroupId;
        }
        return SendMessageBatchRequestEntry.builder()
                .messageBody(entry.getMessageBody())
                .id(entry.getId())
                .delaySeconds(inputDelaySeconds)
                .messageGroupId(inputMessageGroupId)
                .build();
    }

    public SendMessageBatchResponse sendMessageBatchByQueueUrl(@NotBlank String queueUrl, @NotNull Integer delaySeconds, String messageGroupId,
                                                               @NotNull SqsSendMessageBatchRequestEntries entries) throws AppException {
        return sendMessageBatchByQueueUrlAndEntries(queueUrl, buildSendMessageBatchRequestEntries(entries, delaySeconds,
                messageGroupId, StringUtils.endsWith(queueUrl, AwsConst.SQS_SUFFIX_FIFO)));
    }

    public SendMessageBatchResponse sendMessageBatchByQueueName(@NotBlank String queueName, @NotNull Integer delaySeconds, String messageGroupId,
                                                                @NotNull SqsSendMessageBatchRequestEntries entries) throws AppException {
        return sendMessageBatchByQueueUrl(getQueueUrl(queueName).queueUrl(), delaySeconds, messageGroupId, entries);
    }

    private SendMessageBatchResponse sendMessageBatchByQueueUrlAndEntries(@NotBlank String queueUrl, @NotEmpty Collection<SendMessageBatchRequestEntry> entries) throws AppException {
        try {
            return sqsClient.sendMessageBatch(SendMessageBatchRequest.builder()
                    .queueUrl(queueUrl)
                    .entries(entries)
                    .build());
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    private SendMessageResponse sendMessageByQueueUrl(@NotBlank String queueUrl, String message, @NotNull Integer delaySeconds, String messageGroupId) throws AppException {
        try {
            boolean fifoQueue = StringUtils.endsWith(queueUrl, AwsConst.SQS_SUFFIX_FIFO);
            String inputMessageGroupId;
            if (!fifoQueue) {
                inputMessageGroupId = null;
            } else {
                Assertions.assertTrue(StringUtils.isNotBlank(messageGroupId), "messageGroupId must not be blank");
                inputMessageGroupId = messageGroupId;
            }
            return sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .delaySeconds(delaySeconds)
                    .messageGroupId(inputMessageGroupId)
                    .build()
            );
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public SendMessageResponse sendMessageByQueueName(@NotBlank String queueName, String message, @NotNull Integer delaySeconds, String messageGroupId) throws AppException {
        return sendMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), message, delaySeconds, messageGroupId);
    }

    public DeleteQueueResponse deleteQueueByQueueName(@NotBlank String queueName) throws AppException {
        return deleteQueueByQueueUrl(getQueueUrl(queueName).queueUrl());
    }

    public DeleteQueueResponse deleteQueueByQueueUrl(@NotBlank String queueUrl) throws AppException {
        try {
            return sqsClient.deleteQueue(DeleteQueueRequest.builder()
                    .queueUrl(queueUrl)
                    .build());

        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public GetQueueUrlResponse getQueueUrl(@NotBlank String queueName) throws AppException {
        try {
            return sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build());
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueName '%s'", queueName));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public CreateQueueResponse createQueue(@NotBlank String queueName) throws AppException {
        Map<QueueAttributeName, String> queueAttributes = new EnumMap<>(QueueAttributeName.class);
        if (StringUtils.endsWith(queueName, AwsConst.SQS_SUFFIX_FIFO)) {
            queueAttributes.put(QueueAttributeName.FIFO_QUEUE, Boolean.TRUE.toString());
            queueAttributes.put(QueueAttributeName.CONTENT_BASED_DEDUPLICATION, Boolean.TRUE.toString());
        }
        return createQueue(queueName, queueAttributes);
    }

    private CreateQueueResponse createQueue(@NotBlank String queueName, Map<QueueAttributeName, String> attributes) throws AppException {
        try {
            return sqsClient.createQueue(CreateQueueRequest.builder()
                    .queueName(queueName)
                    .attributes(attributes)
                    .build());
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public ReceiveMessageResponse receiveMessageByQueueUrl(@NotBlank String queueUrl, @NotNull Integer maxNumberOfMessages) throws AppException {
        try {
            return sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(maxNumberOfMessages)
                    .build());
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }
    }

    public ReceiveMessageResponse receiveMessageByQueueName(@NotBlank String queueName, @NotNull Integer maxNumberOfMessages) throws AppException {
        return receiveMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), maxNumberOfMessages);
    }

    public DeleteMessageBatchResponse deleteMessageBatchByQueueName(@NotBlank String queueName, @NotNull SqsDeleteMessageBatchRequestEntries messages) throws AppException {
        return deleteMessageBatchByQueueUrl(getQueueUrl(queueName).queueUrl(), messages);
    }

    private DeleteMessageBatchResponse deleteMessageBatchByQueueUrl(@NotBlank String queueUrl, @NotNull SqsDeleteMessageBatchRequestEntries messages) throws AppException {
        return deleteMessageBatchByQueueUrlAndEntries(queueUrl, buildDeleteMessageBatchRequestEntries(messages));
    }

    private Collection<DeleteMessageBatchRequestEntry> buildDeleteMessageBatchRequestEntries(@NotNull SqsDeleteMessageBatchRequestEntries messages) {
        return messages.getSqsDeleteMessageBatchRequestEntries().stream().map(this::buildDeleteMessageBatchRequestEntry).toList();
    }

    private DeleteMessageBatchRequestEntry buildDeleteMessageBatchRequestEntry(@NotNull SqsDeleteMessageBatchRequestEntry message) {
        return DeleteMessageBatchRequestEntry.builder()
                .receiptHandle(message.getReceiptHandle())
                .id(message.getId())
                .build();
    }

    private DeleteMessageBatchResponse deleteMessageBatchByQueueUrlAndEntries(@NotBlank String queueUrl, @NotEmpty Collection<DeleteMessageBatchRequestEntry> entries) throws AppException {
        try {
            return sqsClient.deleteMessageBatch(DeleteMessageBatchRequest.builder()
                    .queueUrl(queueUrl)
                    .entries(entries)
                    .build());
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
        }

    }

    public DeleteMessageResponse deleteMessageByQueueName(@NotBlank String queueName, @NotBlank String receiptHandle) throws AppException {
        return deleteMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), receiptHandle);
    }

    public DeleteMessageResponse deleteMessageByQueueUrl(@NotBlank String queueUrl, @NotBlank String receiptHandle) throws AppException {
        try {
            return sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(receiptHandle)
                    .build());
        } catch (QueueDoesNotExistException e) {
            throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
        } catch (AwsServiceException e) {
            throw AWSExceptionUtils.buildAppException(e, StringUtils.EMPTY);
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
