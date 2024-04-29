package com.eric6166.base.utils;

import com.eric6166.base.config.tracing.AppTraceIdContext;
import com.eric6166.base.dto.AppResponse;
import com.eric6166.base.exception.ErrorDetail;
import com.eric6166.base.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BaseUtils {

    AppTraceIdContext appTraceIdContext;
    HttpServletRequest httpServletRequest;

    public static String getRootCauseMessage(Exception e) {
        return ExceptionUtils.getRootCause(e).getMessage();
    }

    public ResponseEntity<Object> buildResponseExceptionEntity(ErrorResponse errorResponse) {
        if (ObjectUtils.isNotEmpty(appTraceIdContext)) {
            errorResponse.setTraceId(appTraceIdContext.getTraceId());
        }
        return new ResponseEntity<>(new AppResponse<>(errorResponse), errorResponse.getHttpStatus());
    }

    public ResponseEntity<Object> buildInternalServerErrorResponseExceptionEntity(Exception e) {
        log.info("e: {} , errorMessage: {}", e.getClass().getName(), e.getMessage());
        var errorResponse = buildInternalServerErrorResponse(e.getMessage());
        return buildResponseExceptionEntity(errorResponse);
    }

    public ErrorResponse buildInternalServerErrorResponse(String message) {

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public ErrorResponse buildErrorResponse(HttpStatus httpStatus, String message) {
        return buildErrorResponse(httpStatus, httpStatus.name(), message, null);
    }

    public ErrorResponse buildErrorResponse(HttpStatus httpStatus, String error, String message, List<ErrorDetail> errorDetails) {
        log.info("e: {} , errorMessage: {}", error, message);
        return new ErrorResponse(httpStatus, error,
                message, httpServletRequest, errorDetails);
    }





}
