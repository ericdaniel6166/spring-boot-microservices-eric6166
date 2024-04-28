package com.eric6166.common.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

@Slf4j
public class KafkaErrorHandler implements CommonErrorHandler {
    @Override
    public boolean handleOne(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
        log.info("Kafka error handler for message: {}", record.value().toString()); // comment // for local testing
        return CommonErrorHandler.super.handleOne(thrownException, record, consumer, container);
    }

    @Override
    public void handleOtherException(Exception thrownException, Consumer<?, ?> consumer, MessageListenerContainer container, boolean batchListener) {
        log.info("Kafka error handler for consumer: {}", consumer); // comment // for local testing
        CommonErrorHandler.super.handleOtherException(thrownException, consumer, container, batchListener);
    }
}
