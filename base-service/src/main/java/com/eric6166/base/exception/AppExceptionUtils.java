package com.eric6166.base.exception;

import com.eric6166.base.utils.BaseUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public final class AppExceptionUtils {

    HttpServletRequest httpServletRequest;

    public static Object getAppExceptionRootCause(AppException e) {
        return e.getRootCause() == null ? BaseUtils.getRootCauseMessage(e) : e.getRootCause();
    }

    public ErrorResponse buildErrorResponse(HttpStatus httpStatus, Exception e) {
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
