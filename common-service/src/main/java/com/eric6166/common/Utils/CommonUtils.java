package com.eric6166.common.Utils;

import com.eric6166.base.exception.AppException;
import com.eric6166.common.config.kafka.AppEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
public final class CommonUtils {

    private CommonUtils() {
        throw new IllegalStateException("Utility class");
    }

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
