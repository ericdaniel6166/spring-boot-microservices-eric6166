package com.eric6166.base.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime timestamp;

    private int status;

    @JsonIgnore
    private HttpStatusCode httpStatus;

    private String traceId;

    private String error;

    private String message;
    private String path;
    private Object rootCause;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(HttpStatusCode httpStatus, String error, String message, HttpServletRequest httpServletRequest, Object rootCause) {
        this();
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.error = error;
        this.message = message;
        this.path = httpServletRequest.getRequestURI();
        this.rootCause = rootCause;
    }

}
