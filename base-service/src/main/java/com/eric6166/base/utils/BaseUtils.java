package com.eric6166.base.utils;

import com.eric6166.base.config.tracing.AppTraceIdContext;
import com.eric6166.base.dto.AppResponse;
import com.eric6166.base.exception.ErrorResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BaseUtils {

    AppTraceIdContext appTraceIdContext;

    public static String getRootCauseMessage(Throwable e) {
        return ExceptionUtils.getRootCause(e).getMessage();
    }

    public ResponseEntity<Object> buildResponseExceptionEntity(ErrorResponse errorResponse) {
        if (ObjectUtils.isNotEmpty(appTraceIdContext)) {
            errorResponse.setTraceId(appTraceIdContext.getTraceId());
        }
        return new ResponseEntity<>(new AppResponse<>(errorResponse), errorResponse.getHttpStatus());
    }


}
