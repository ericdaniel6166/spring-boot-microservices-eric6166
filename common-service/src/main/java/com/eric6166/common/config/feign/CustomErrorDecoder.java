package com.eric6166.common.config.feign;

import com.eric6166.common.exception.AppException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        var httpStatus = HttpStatus.valueOf(response.status());
        return new AppException(httpStatus, httpStatus.name(), response.reason());
    }
}
