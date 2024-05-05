package com.eric6166.aws.dto;

import com.eric6166.base.exception.ErrorResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatusCode;

@EqualsAndHashCode(callSuper = false)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AWSErrorResponse extends ErrorResponse {

    String requestId;
    String extendedRequestId;

    public AWSErrorResponse(HttpStatusCode httpStatus, String error, String message, String path, Object rootCause, String requestId, String extendedRequestId) {
        super(httpStatus, error, message, path, rootCause);
        this.requestId = requestId;
        this.extendedRequestId = extendedRequestId;
    }

}
