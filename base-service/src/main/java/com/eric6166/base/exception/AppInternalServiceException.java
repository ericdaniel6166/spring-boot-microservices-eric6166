package com.eric6166.base.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class AppInternalServiceException extends AppException {

    public AppInternalServiceException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, StringUtils.isNotBlank(message) ? message : HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    public AppInternalServiceException(Object rootCause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), rootCause);
    }

}
