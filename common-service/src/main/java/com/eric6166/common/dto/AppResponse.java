package com.eric6166.common.dto;

import com.eric6166.common.config.exception.ErrorResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppResponse<T> {
    T data;
    ErrorResponse error;

    public AppResponse(T data) {
        this.data = data;
    }

    public AppResponse(ErrorResponse error) {
        this.error = error;
    }

}
