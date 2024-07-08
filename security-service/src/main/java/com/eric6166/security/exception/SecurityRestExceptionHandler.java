package com.eric6166.security.exception;

import com.eric6166.base.exception.AppExceptionUtils;
import com.eric6166.base.utils.BaseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityRestExceptionHandler {

    private final BaseUtils baseUtils;
    private final AppExceptionUtils appExceptionUtils;

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        var rootCause = BaseUtils.getRootCauseMessage(e);
        log.info("e: {} , rootCause: {}", e.getClass().getName(), rootCause); // comment // for local testing
        var errorResponse = appExceptionUtils.buildErrorResponse(HttpStatus.FORBIDDEN, e);
        return baseUtils.buildResponseExceptionEntity(errorResponse);
    }
}
