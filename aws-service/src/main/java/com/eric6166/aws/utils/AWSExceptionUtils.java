package com.eric6166.aws.utils;

import com.eric6166.aws.dto.AWSErrorResponse;
import com.eric6166.base.exception.AppException;
import com.eric6166.base.exception.AppNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.exception.AwsServiceException;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public final class AWSExceptionUtils {

    public static AppException buildAppException(AwsServiceException e, HttpStatus httpStatus, String errorCode, String errorMessage) {
        var awsErrorDetails = e.awsErrorDetails();
        if (awsErrorDetails != null) {
            var httpStatusResponse = HttpStatus.valueOf(e.statusCode());
            var errorCodeResponse = httpStatusResponse.name();
            var errorMessageResponse = httpStatusResponse.getReasonPhrase();
            if (httpStatus != null) {
                httpStatusResponse = httpStatus;
            }
            if (StringUtils.isNotBlank(errorCode)) {
                errorCodeResponse = errorCode;
            }
            if (StringUtils.isNotBlank(errorMessage)) {
                errorMessageResponse = errorMessage;
            }
            return new AppException(httpStatusResponse, errorCodeResponse, errorMessageResponse, buildAWSErrorResponse(e));
        }
        return new AppException(e);

    }

    public static AWSErrorResponse buildAWSErrorResponse(AwsServiceException e) {
        var awsErrorDetails = e.awsErrorDetails();
        return new AWSErrorResponse(HttpStatus.valueOf(e.statusCode()), awsErrorDetails.errorCode(),
                awsErrorDetails.errorMessage(), null, null, e.requestId(), e.extendedRequestId());
    }

    public static String buildErrorMessage(AwsServiceException e, String resource) {
        var httpStatus = HttpStatus.valueOf(e.statusCode());
        String errorMessage ;
        switch (httpStatus) {
            case MOVED_PERMANENTLY -> errorMessage = String.format("%s is not available", resource);
            default -> errorMessage = e.awsErrorDetails().errorMessage();
        }
        return errorMessage;
    }

    public static AppException buildAppException(AwsServiceException awsServiceException, AppException appException) {
        return buildAppException(awsServiceException, appException.getHttpStatus(), appException.getHttpStatus().name(), appException.getMessage());
    }

    public static AppException buildAppException(AwsServiceException e) {
        var httpStatus = HttpStatus.valueOf(e.statusCode());
        return buildAppException(e, httpStatus, httpStatus.name(), e.awsErrorDetails().errorMessage());
    }

    public static AppException buildAppNotFoundException(AwsServiceException e, String resource) {
        return buildAppException(e, new AppNotFoundException(resource));
    }


}