package com.eric6166.common.config.feign;

import com.eric6166.common.exception.AppBadRequestException;
import com.eric6166.common.exception.AppException;
import com.eric6166.common.exception.AppInternalServiceException;
import com.eric6166.common.exception.AppNotFoundException;
import com.eric6166.common.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new AppBadRequestException(response.reason());
            case 404 -> new AppNotFoundException(response.reason());
            case 500 -> new AppInternalServiceException(response.reason());
            default -> new AppException(ErrorCode.GENERIC_ERROR.name(), response.reason());
        };
    }
}
