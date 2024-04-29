package com.eric6166.security.exception;

import com.eric6166.base.exception.ErrorResponse;
import com.eric6166.base.utils.BaseUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityRestExceptionHandler {

    BaseUtils baseUtils;

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest httpServletRequest) {
        String errorMessage = BaseUtils.getRootCauseMessage(e);
        log.debug("e: {} , errorMessage: {}", e.getClass().getName(), errorMessage); // comment // for local testing
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.name(),
                HttpStatus.FORBIDDEN.getReasonPhrase(), httpServletRequest, null);
        return baseUtils.buildResponseExceptionEntity(errorResponse);
    }
}
