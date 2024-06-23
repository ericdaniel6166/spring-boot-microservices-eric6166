package com.eric6166.aws.utils;

import com.eric6166.aws.dto.AWSErrorResponse;
import com.eric6166.base.exception.AppBadRequestException;
import com.eric6166.base.exception.AppException;
import com.eric6166.base.exception.AppInternalServiceException;
import com.eric6166.base.exception.AppNotFoundException;
import com.eric6166.base.utils.BaseUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import software.amazon.awssdk.awscore.exception.AwsServiceException;

public final class AWSExceptionUtils {

    public static AWSErrorResponse buildAWSErrorResponse(AwsServiceException e) {
        var awsErrorDetails = e.awsErrorDetails();
        return new AWSErrorResponse(HttpStatus.valueOf(e.statusCode()), awsErrorDetails.errorCode(),
                awsErrorDetails.errorMessage(), null, null, e.requestId(), e.extendedRequestId());
    }

    public static AppException buildAppException(AwsServiceException awsServiceException, AppException appException) {
        Object rootCause = null;
        if (awsServiceException.awsErrorDetails() != null) {
            rootCause = buildAWSErrorResponse(awsServiceException);
        }
        return new AppException(appException.getHttpStatus(), appException.getError(), appException.getMessage(),
                rootCause != null ? rootCause : BaseUtils.getRootCauseMessage(awsServiceException));
    }

    public static AppException buildAppException(AwsServiceException e) {
        return buildAppException(e, e.getMessage());
    }

    public static AppException buildAppException(AwsServiceException e, String message) {
        var httpStatus = HttpStatus.valueOf(e.statusCode());
        if (httpStatus.is5xxServerError()) {
            return buildAppException(e, new AppInternalServiceException(message));
        }
        return buildAppException(e, new AppBadRequestException(message));
    }

    public static AppException buildAppNotFoundException(AwsServiceException e, String resource) {
        return buildAppException(e, new AppNotFoundException(resource));
    }
}
