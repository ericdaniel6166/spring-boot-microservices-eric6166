package com.eric6166.base.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
        this.timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }

    public ErrorResponse(HttpStatusCode httpStatus, String error, String message, String path, Object rootCause) {
        this();
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.error = error;
        this.message = message;
        this.path = path;
        this.rootCause = rootCause;
    }

}
