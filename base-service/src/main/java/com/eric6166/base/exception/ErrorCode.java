package com.eric6166.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    GENERIC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, "Generic Error"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "Validation Error"),
    ;

    private final int value;
    private final HttpStatus httpStatus;
    private final String reasonPhrase;

}
