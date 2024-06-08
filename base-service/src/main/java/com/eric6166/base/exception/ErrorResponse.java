package com.eric6166.base.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDateTime timestamp;

    int status;

    @JsonIgnore
    HttpStatusCode httpStatus;

    String traceId;

    String error;

    String message;
    String path;
    Object rootCause;

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
