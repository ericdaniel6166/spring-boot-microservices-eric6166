package com.eric6166.common.exception;

import com.eric6166.base.exception.ErrorDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppException extends Exception {

    String error;

    HttpStatus httpStatus;

    int status;

    List<ErrorDetail> errorDetails;

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

    public AppException(HttpStatus httpStatus, String error, String message, List<ErrorDetail> errorDetails) {
        super(message);
        this.error = error;
        this.errorDetails = errorDetails;
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
    }

    public AppException(Throwable cause) {
        super(cause);
        this.error = HttpStatus.INTERNAL_SERVER_ERROR.name();
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}