package com.eric6166.base.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class AppNotFoundException extends AppException {

    public AppNotFoundException(String resource) {
        super(HttpStatus.NOT_FOUND, StringUtils.isNotBlank(resource) ? String.format("%s not found", resource) : HttpStatus.NOT_FOUND.getReasonPhrase());
    }


}
