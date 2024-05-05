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
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

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

    public static AppException buildAppException(AwsServiceException awsServiceException, AppException appException) {
        return buildAppException(awsServiceException, appException.getHttpStatus(), appException.getHttpStatus().name(), appException.getMessage());
    }

    public static AppException buildAppException(NoSuchBucketException e, String bucket) {
        return buildAppException(e, new AppNotFoundException(String.format("bucket with name '%s'", bucket)));
    }


}
