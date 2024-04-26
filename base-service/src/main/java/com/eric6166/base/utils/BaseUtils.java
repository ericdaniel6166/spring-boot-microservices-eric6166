package com.eric6166.base.utils;

import com.eric6166.base.dto.AppResponse;
import com.eric6166.base.exception.ErrorResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;

public final class BaseUtils {

    public static ResponseEntity<Object> buildResponseExceptionEntity(ErrorResponse errorResponse) {
//        if (ObjectUtils.isNotEmpty(appTraceIdContext)) {
//            errorResponse.setTraceId(appTraceIdContext.getTraceId());
//        }
        return new ResponseEntity<>(new AppResponse<>(errorResponse), errorResponse.getHttpStatus());
    }

    public static String getRootCauseMessage(Exception e) {
        return ExceptionUtils.getRootCause(e).getMessage();
    }
}
