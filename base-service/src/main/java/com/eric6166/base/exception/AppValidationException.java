package com.eric6166.base.exception;

import java.util.List;

public class AppValidationException extends AppException {

    public AppValidationException(List<ErrorDetail> errorDetails) {
        super(ErrorCode.VALIDATION_ERROR.getHttpStatus(), ErrorCode.VALIDATION_ERROR.name(), ErrorCode.VALIDATION_ERROR.getReasonPhrase(), errorDetails);
    }

    public AppValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR.getHttpStatus(), message);
    }

}
