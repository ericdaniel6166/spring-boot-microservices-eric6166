package com.eric6166.common.config.kafka;

import com.eric6166.base.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class KafkaUtils {

    public static void handleSendResult(AppEvent appEvent, CompletableFuture<SendResult<String, Object>> sendResult) throws AppException {
        sendResult.whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.info("Failed to send appEvent, topic: {}, uuid: {}", result.getProducerRecord().topic(), appEvent.getUuid(), throwable);
            } else {
                log.debug("appEvent sent, topic: {}, uuid :{}", result.getProducerRecord().topic() , appEvent.getUuid());
            }
        });
        try {
            sendResult.join();
        } catch (CompletionException e) {
            throw new AppException(e.getCause());
        }
    }
}
