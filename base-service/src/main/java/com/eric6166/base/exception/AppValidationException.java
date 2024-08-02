package com.eric6166.base.exception;


public class AppValidationException extends AppException {

    public AppValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR.getHttpStatus(), ErrorCode.VALIDATION_ERROR.name(), message);
    }

    public AppValidationException(String message, Object rootCause) {
        super(ErrorCode.VALIDATION_ERROR.getHttpStatus(), ErrorCode.VALIDATION_ERROR.name(), message, rootCause);
    }

}
