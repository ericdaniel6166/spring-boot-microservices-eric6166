package com.eric6166.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppResponse<T> {
    T data;
    List<String> errors;

    public AppResponse(T data) {
        this.data = data;
    }

    public AppResponse(List<String> errors) {
        this.errors = errors;
    }
}
