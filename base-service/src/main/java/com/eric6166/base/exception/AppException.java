package com.eric6166.base.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AppException extends Exception {

    private Object rootCause;
    private String error;

    private HttpStatus httpStatus;

    private int status;

    public AppException(HttpStatus httpStatus, String message) {
        super(message);
        this.error = httpStatus.name();
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
    }

    public AppException(HttpStatus httpStatus, String error, String message) {
        super(message);
        this.error = error;
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
    }

    public AppException(HttpStatus httpStatus, String error, String message, Object rootCause) {
        super(message);
        this.error = error;
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.rootCause = rootCause;
    }

    public AppException(Throwable cause) {
        super(cause);
        this.error = HttpStatus.INTERNAL_SERVER_ERROR.name();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}