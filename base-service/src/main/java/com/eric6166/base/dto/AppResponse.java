package com.eric6166.base.dto;

import com.eric6166.base.exception.ErrorResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppResponse<T> {
    private T data;
    private ErrorResponse error;

    public AppResponse(T data) {
        this.data = data;
    }

    public AppResponse(ErrorResponse error) {
        this.error = error;
    }

}
