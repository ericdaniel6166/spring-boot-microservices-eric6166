package com.eric6166.common.exception;

import com.eric6166.base.exception.ErrorResponse;
import com.eric6166.base.utils.BaseUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public final class AppExceptionUtils {

    BaseUtils baseUtils;
    HttpServletRequest httpServletRequest;

    public static Object getAppExceptionRootCause(AppException e) {
        return e.getRootCause() == null ? BaseUtils.getRootCauseMessage(e) : e.getRootCause();
    }

    public ResponseEntity<Object> buildInternalServerErrorResponseExceptionEntity(Exception e) {
        log.debug("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage());
        var errorResponse = buildInternalServerErrorResponse(e.getMessage(), BaseUtils.getRootCauseMessage(e));
        return baseUtils.buildResponseExceptionEntity(errorResponse);
    }

    public ErrorResponse buildInternalServerErrorResponse(String message, Object rootCause) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, rootCause);
    }

    public ErrorResponse buildErrorResponse(HttpStatus httpStatus, String message, Object rootCause) {
        return buildErrorResponse(httpStatus, httpStatus.name(), message, rootCause);
    }

    public ErrorResponse buildErrorResponse(HttpStatus httpStatus, String error, String message, Object rootCause) {
        log.debug("e: {} , errorMessage: {}, rootCause: {}", error, message, rootCause);
        return new ErrorResponse(httpStatus, error,
                message, httpServletRequest, rootCause);
    }
}
