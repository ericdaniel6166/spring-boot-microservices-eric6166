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

    public AppException(String error, String message) {
        super(message);
        this.error = error;
    }

    public AppException(HttpStatus httpStatus, String error, String message, List<ErrorDetail> errorDetails) {
        super(message);
        this.error = error;
        this.errorDetails = errorDetails;
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
    }

}