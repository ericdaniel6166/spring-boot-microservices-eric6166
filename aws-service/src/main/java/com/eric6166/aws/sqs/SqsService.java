package com.eric6166.aws.sqs;


import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContextOrSamplingFlags;
import com.eric6166.aws.utils.AWSExceptionUtils;
import com.eric6166.base.exception.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
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
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ConditionalOnProperty(name = "cloud.aws.sqs.enabled", havingValue = "true")
public class SqsService {

    Tracer tracer;
    SqsClient sqsClient;
    SqsProps sqsProps;

    public Collection<SendMessageBatchRequestEntry> buildSendMessageBatchRequestEntry(SqsMessages messages) {
        var delaySeconds = messages.getDelaySeconds();
        var messageGroupId = messages.getMessageGroupId();
        return messages.getSqsMessages().stream().map(o -> buildSendMessageBatchRequestEntry(o, delaySeconds, messageGroupId)).collect(Collectors.toList());
    }

    public SendMessageBatchRequestEntry buildSendMessageBatchRequestEntry(SqsMessage message, Integer delaySeconds, String messageGroupId) {
        Integer inputDelaySeconds;
        if (message.getDelaySeconds() != null) {
            inputDelaySeconds = message.getDelaySeconds();
        } else if (delaySeconds != null) {
            inputDelaySeconds = delaySeconds;
        } else {
            inputDelaySeconds = sqsProps.getTemplate().getDelaySeconds();
        }
        String inputMessageGroupId;
        if (StringUtils.isNotBlank(message.getMessageGroupId())) {
            inputMessageGroupId = message.getMessageGroupId();
        } else if (StringUtils.isNotBlank(messageGroupId)) {
            inputMessageGroupId = messageGroupId;
        } else {
            inputMessageGroupId = sqsProps.getTemplate().getQueue().getFifo().getMessageGroupId();
        }
        return SendMessageBatchRequestEntry.builder()
                .messageBody(message.getMessageBody())
                .id(message.getId())
                .delaySeconds(inputDelaySeconds)
                .messageGroupId(inputMessageGroupId)
                .build();
    }

    public SendMessageBatchResponse sendBatchMessageByQueueUrl(String queueUrl, SqsMessages messages) throws AppException {
        return sendBatchMessageByQueueUrlAndRequestEntries(queueUrl, buildSendMessageBatchRequestEntry(messages));
    }

    public SendMessageBatchResponse sendBatchMessageByQueueName(String queueName, SqsMessages messages) throws AppException {
        return sendBatchMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), messages);
    }

    public SendMessageBatchResponse sendBatchMessageByQueueUrlAndRequestEntries(String queueUrl, Collection<SendMessageBatchRequestEntry> entries) throws AppException {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("sendBatchMessageByQueueUrlAndRequestEntries").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return sqsClient.sendMessageBatch(SendMessageBatchRequest.builder()
                        .queueUrl(queueUrl)
                        .entries(entries)
                        .build());
            } catch (QueueDoesNotExistException e) {
                throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
            }
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    public SendMessageResponse sendMessageByQueueUrl(String queueUrl, String message, Integer delaySeconds) throws AppException {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("sendMessageByQueueUrl").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                var inputDelaySeconds = delaySeconds == null ? sqsProps.getTemplate().getDelaySeconds() : delaySeconds;
                return sqsClient.sendMessage(SendMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .messageBody(message)
                        .delaySeconds(inputDelaySeconds)
                        .build()
                );
            } catch (QueueDoesNotExistException e) {
                throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
            }
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    public SendMessageResponse sendMessageByQueueName(String queueName, String message, Integer delaySeconds) throws AppException {
        return sendMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), message, delaySeconds);
    }

    public DeleteQueueResponse deleteQueueByQueueName(String queueName) throws AppException {
        return deleteQueueByQueueUrl(getQueueUrl(queueName).queueUrl());
    }

    public DeleteQueueResponse deleteQueueByQueueUrl(String queueUrl) throws AppException {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("deleteQueueByQueueUrl").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return sqsClient.deleteQueue(DeleteQueueRequest.builder()
                        .queueUrl(queueUrl)
                        .build());

            } catch (QueueDoesNotExistException e) {
                throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
            }
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    public GetQueueUrlResponse getQueueUrl(String queueName) throws AppException {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("getQueueUrl").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                        .queueName(queueName)
                        .build());
            } catch (QueueDoesNotExistException e) {
                throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueName '%s'", queueName));
            }
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    public CreateQueueResponse createQueue(String queueName, Map<QueueAttributeName, String> attributes) {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("createQueue").start();
        try (var ws = tracer.withSpanInScope(span)) {
            return sqsClient.createQueue(CreateQueueRequest.builder()
                    .queueName(queueName)
                    .attributes(attributes)
                    .build());
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    public ReceiveMessageResponse receiveMessageByQueueUrl(String queueUrl, Integer maxNumberOfMessages) throws AppException {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("receiveMessageByQueueUrl").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                var inputMaxNumberOfMessages = maxNumberOfMessages == null ? sqsProps.getTemplate().getMaxNumberOfMessages() : maxNumberOfMessages;
                return sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .maxNumberOfMessages(inputMaxNumberOfMessages)
                        .build());
            } catch (QueueDoesNotExistException e) {
                throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueUrl '%s'", queueUrl));
            } catch (SqsException e) {
                throw AWSExceptionUtils.buildAppException(e);
            }
        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    public ReceiveMessageResponse receiveMessageByQueueName(String queueName, Integer maxNumberOfMessages) throws AppException {
        return receiveMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), maxNumberOfMessages);
    }

    void test() {
        Span span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("test").start();
        try (var ws = tracer.withSpanInScope(span)) {

        } catch (RuntimeException e) {
            log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage()); // comment // for local testing
            span.error(e);
            throw e;
        } finally {
            span.finish();
        }
    }

    //listQueues
    //deleteMessage

}
