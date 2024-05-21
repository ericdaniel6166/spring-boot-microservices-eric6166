package com.eric6166.aws.utils;

import com.eric6166.aws.dto.AWSErrorResponse;
import com.eric6166.base.exception.AppBadRequestException;
import com.eric6166.base.exception.AppException;
import com.eric6166.base.exception.AppExceptionUtils;
import com.eric6166.base.exception.AppInternalServiceException;
import com.eric6166.base.exception.AppNotFoundException;
import com.eric6166.base.utils.BaseUtils;
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
                rootCause == null ? BaseUtils.getRootCauseMessage(awsServiceException) : rootCause);
    }

    public static AppException buildAppException(AwsServiceException e) {
        return buildAppException(e, StringUtils.EMPTY);
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
