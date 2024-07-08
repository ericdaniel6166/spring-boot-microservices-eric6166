package com.eric6166.aws.dto;

import com.eric6166.base.exception.ErrorResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class AWSErrorResponse extends ErrorResponse {

    private String requestId;
    private String extendedRequestId;

    public AWSErrorResponse(HttpStatusCode httpStatus, String error, String message, String path, Object rootCause, String requestId, String extendedRequestId) {
        super(httpStatus, error, message, path, rootCause);
        this.requestId = requestId;
        this.extendedRequestId = extendedRequestId;
    }

}
