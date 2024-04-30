package com.eric6166.base.exception;

import org.springframework.http.HttpStatus;

public class AppBadRequestException extends AppException {

    public AppBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.name(), message);
    }


}
