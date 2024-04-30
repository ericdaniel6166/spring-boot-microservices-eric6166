package com.eric6166.common.exception;

import com.eric6166.base.exception.ErrorDetail;

import java.util.List;

public class AppValidationException extends AppException {

    public AppValidationException(List<ErrorDetail> errorDetails) {
        super(ErrorCode.VALIDATION_ERROR.getHttpStatus(), ErrorCode.VALIDATION_ERROR.name(), ErrorCode.VALIDATION_ERROR.getReasonPhrase(), errorDetails);
    }


}
