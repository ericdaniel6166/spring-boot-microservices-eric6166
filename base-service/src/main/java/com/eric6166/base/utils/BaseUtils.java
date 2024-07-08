package com.eric6166.base.utils;

import com.eric6166.base.config.tracing.AppTraceIdContext;
import com.eric6166.base.dto.AppResponse;
import com.eric6166.base.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@RequiredArgsConstructor
@Component
@Slf4j
public class BaseUtils {

    private final AppTraceIdContext appTraceIdContext;

    public static String getRootCauseMessage(Throwable e) {
        return ExceptionUtils.getRootCause(e).getMessage();
    }

    public static String getPathFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getPath();
        } catch (MalformedURLException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public ResponseEntity<Object> buildResponseExceptionEntity(ErrorResponse errorResponse) {
        if (ObjectUtils.isNotEmpty(appTraceIdContext)) {
            errorResponse.setTraceId(appTraceIdContext.getTraceId());
        }
        return new ResponseEntity<>(new AppResponse<>(errorResponse), errorResponse.getHttpStatus());
    }


}
