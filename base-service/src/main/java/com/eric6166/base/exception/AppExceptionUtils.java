package com.eric6166.base.exception;

import com.eric6166.base.utils.BaseUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public final class AppExceptionUtils {

    private final HttpServletRequest httpServletRequest;

    public ErrorResponse buildErrorResponse(HttpStatus httpStatus, Throwable e) {
        return buildErrorResponse(httpStatus, httpStatus.name(), httpStatus.getReasonPhrase(), BaseUtils.getRootCauseMessage(e));
    }

    public ErrorResponse buildErrorResponse(HttpStatus httpStatus, String error, String message, Object rootCause) {
        log.info("error: {} , errorMessage: {}, rootCause: {}", error, message, rootCause);
        return new ErrorResponse(httpStatus, error, message, httpServletRequest.getRequestURI(), rootCause);
    }

    public ErrorResponse buildErrorResponse(ErrorCode errorCode, Object rootCause) {
        log.info("error: {}, errorMessage: {}, rootCause: {}", errorCode.name(), errorCode.getReasonPhrase(), rootCause);
        return new ErrorResponse(errorCode.getHttpStatus(), errorCode.name(), errorCode.getReasonPhrase(),
                httpServletRequest.getRequestURI(), rootCause);
    }

}
