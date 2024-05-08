package com.eric6166.aws.sqs;


import brave.Tracer;
import brave.propagation.TraceContextOrSamplingFlags;
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
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.Collection;
import java.util.EnumMap;
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

    private Collection<SendMessageBatchRequestEntry> buildSendMessageBatchRequestEntries(SqsSendMessages messages, boolean fifoQueue) {
        var delaySeconds = messages.getDelaySeconds();
        var messageGroupId = messages.getMessageGroupId();
        return messages.getSqsMessages().stream().map(o -> buildSendMessageBatchRequestEntry(o, delaySeconds, messageGroupId, fifoQueue)).collect(Collectors.toList());
    }

    private SendMessageBatchRequestEntry buildSendMessageBatchRequestEntry(SqsSendMessage message, Integer delaySeconds, String messageGroupId, boolean fifoQueue) {
        Integer inputDelaySeconds;
        if (message.getDelaySeconds() != null) {
            inputDelaySeconds = message.getDelaySeconds();
        } else if (delaySeconds != null) {
            inputDelaySeconds = delaySeconds;
        } else {
            inputDelaySeconds = sqsProps.getTemplate().getDelaySeconds();
        }
        String inputMessageGroupId;
        if (!fifoQueue) {
            inputMessageGroupId = null;
        } else if (StringUtils.isNotBlank(message.getMessageGroupId())) {
            inputMessageGroupId = message.getMessageGroupId();
        } else if (StringUtils.isNotBlank(messageGroupId)) {
            inputMessageGroupId = messageGroupId;
        } else {
            inputMessageGroupId = sqsProps.getTemplate().getMessageGroupId();
        }
        return SendMessageBatchRequestEntry.builder()
                .messageBody(message.getMessageBody())
                .id(message.getId())
                .delaySeconds(inputDelaySeconds)
                .messageGroupId(inputMessageGroupId)
                .build();
    }

    public SendMessageBatchResponse sendBatchMessageByQueueUrl(String queueUrl, SqsSendMessages messages) throws AppException {
        return sendBatchMessageByQueueUrlAndEntries(queueUrl, buildSendMessageBatchRequestEntries(messages, StringUtils.endsWith(queueUrl, AwsConst.SQS_SUFFIX_FIFO)));
    }

    public SendMessageBatchResponse sendBatchMessageByQueueName(String queueName, SqsSendMessages messages) throws AppException {
        return sendBatchMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), messages);
    }

    public SendMessageBatchResponse sendBatchMessageByQueueUrlAndEntries(String queueUrl, Collection<SendMessageBatchRequestEntry> entries) throws AppException {
        var span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("sendBatchMessageByQueueUrlAndRequestEntries").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return sqsClient.sendMessageBatch(SendMessageBatchRequest.builder()
                        .queueUrl(queueUrl)
                        .entries(entries)
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

    public SendMessageResponse sendMessageByQueueUrl(String queueUrl, String message, Integer delaySeconds, String messageGroupId) throws AppException {
        var span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("sendMessageByQueueUrl").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                var inputDelaySeconds = delaySeconds == null ? sqsProps.getTemplate().getDelaySeconds() : delaySeconds;
                boolean fifoQueue = StringUtils.endsWith(queueUrl, AwsConst.SQS_SUFFIX_FIFO);
                String inputMessageGroupId;
                if (!fifoQueue) {
                    inputMessageGroupId = null;
                } else if (StringUtils.isNotBlank(messageGroupId)) {
                    inputMessageGroupId = messageGroupId;
                } else {
                    inputMessageGroupId = sqsProps.getTemplate().getMessageGroupId();
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
        var span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("deleteQueueByQueueUrl").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return sqsClient.deleteQueue(DeleteQueueRequest.builder()
                        .queueUrl(queueUrl)
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

    public GetQueueUrlResponse getQueueUrl(String queueName) throws AppException {
        var span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("getQueueUrl").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                        .queueName(queueName)
                        .build());
            } catch (QueueDoesNotExistException e) {
                throw AWSExceptionUtils.buildAppNotFoundException(e, String.format("queue with queueName '%s'", queueName));
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

    public CreateQueueResponse createQueue(String queueName) throws AppException {
        Map<QueueAttributeName, String> queueAttributes = new EnumMap<>(QueueAttributeName.class);
        if (StringUtils.endsWith(queueName, AwsConst.SQS_SUFFIX_FIFO)) {
            queueAttributes.put(QueueAttributeName.FIFO_QUEUE, Boolean.TRUE.toString());
            queueAttributes.put(QueueAttributeName.CONTENT_BASED_DEDUPLICATION, Boolean.TRUE.toString());
        }
        return createQueue(queueName, queueAttributes);
    }


    public CreateQueueResponse createQueue(String queueName, Map<QueueAttributeName, String> attributes) throws AppException {
        var span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("createQueue").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return sqsClient.createQueue(CreateQueueRequest.builder()
                        .queueName(queueName)
                        .attributes(attributes)
                        .build());
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

    public ReceiveMessageResponse receiveMessageByQueueUrl(String queueUrl, Integer maxNumberOfMessages) throws AppException {
        var span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("receiveMessageByQueueUrl").start();
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

    public DeleteMessageBatchResponse deleteMessageBatchByQueueName(String queueName, SqsDeleteMessages messages) throws AppException {
        return deleteMessageBatchByQueueUrl(getQueueUrl(queueName).queueUrl(), messages);
    }

    private DeleteMessageBatchResponse deleteMessageBatchByQueueUrl(String queueUrl, SqsDeleteMessages messages) throws AppException {
        return deleteMessageBatchByQueueUrlAndEntries(queueUrl, buildDeleteMessageBatchRequestEntries(messages));
    }

    private Collection<DeleteMessageBatchRequestEntry> buildDeleteMessageBatchRequestEntries(SqsDeleteMessages messages) {
        return messages.getSqsDeleteMessages().stream().map(this::buildDeleteMessageBatchRequestEntry).toList();
    }

    private DeleteMessageBatchRequestEntry buildDeleteMessageBatchRequestEntry(SqsDeleteMessage message) {
        return DeleteMessageBatchRequestEntry.builder()
                .receiptHandle(message.getReceiptHandle())
                .id(message.getId())
                .build();
    }

    public DeleteMessageBatchResponse deleteMessageBatchByQueueUrlAndEntries(String queueUrl, Collection<DeleteMessageBatchRequestEntry> entries) throws AppException {
        var span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("deleteMessageBatchByQueueUrlAndEntries").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return sqsClient.deleteMessageBatch(DeleteMessageBatchRequest.builder()
                        .queueUrl(queueUrl)
                        .entries(entries)
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

    public DeleteMessageResponse deleteMessageByQueueName(String queueName, String receiptHandle) throws AppException {
        return deleteMessageByQueueUrl(getQueueUrl(queueName).queueUrl(), receiptHandle);
    }

    public DeleteMessageResponse deleteMessageByQueueUrl(String queueUrl, String receiptHandle) throws AppException {
        var span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("deleteMessageByQueueUrl").start();
        try (var ws = tracer.withSpanInScope(span)) {
            try {
                return sqsClient.deleteMessage(DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(receiptHandle)
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

    void test() {
        var span = tracer.nextSpan(TraceContextOrSamplingFlags.create(tracer.currentSpan().context())).name("test").start();
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
