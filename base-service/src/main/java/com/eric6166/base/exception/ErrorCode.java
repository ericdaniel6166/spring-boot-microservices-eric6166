package com.eric6166.base.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    GENERIC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Generic Error"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation Error"),
    ;

    HttpStatus httpStatus;
    String reasonPhrase;

}
