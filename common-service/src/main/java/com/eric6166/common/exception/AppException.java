package com.eric6166.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends Exception {

    Object rootCause;
    String error;

    HttpStatus httpStatus;

    int status;

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