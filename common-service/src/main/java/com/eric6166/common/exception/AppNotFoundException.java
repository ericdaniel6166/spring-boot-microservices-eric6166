package com.eric6166.common.exception;

import org.springframework.http.HttpStatus;

public class AppNotFoundException extends AppException {

    public AppNotFoundException(String resource) {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.name(), String.format("%s not found", resource), null);
    }


}
