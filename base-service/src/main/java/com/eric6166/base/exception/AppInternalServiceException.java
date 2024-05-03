package com.eric6166.base.exception;

import org.springframework.http.HttpStatus;

public class AppInternalServiceException extends AppException {

    public AppInternalServiceException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }


}
