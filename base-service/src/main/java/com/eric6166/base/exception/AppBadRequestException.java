package com.eric6166.base.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class AppBadRequestException extends AppException {

    public AppBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, StringUtils.isNotBlank(message) ? message : HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    public AppBadRequestException() {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

}
