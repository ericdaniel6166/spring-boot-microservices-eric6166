package com.eric6166.base.exception;

import org.springframework.http.HttpStatus;

public class AppNotFoundException extends AppException {

    public AppNotFoundException(String resource) {
        super(HttpStatus.NOT_FOUND, String.format("%s not found", resource));
    }


}
