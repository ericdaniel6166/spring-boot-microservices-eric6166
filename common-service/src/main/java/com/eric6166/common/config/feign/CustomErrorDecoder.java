package com.eric6166.common.config.feign;

import com.eric6166.base.exception.AppBadRequestException;
import com.eric6166.base.exception.AppInternalServiceException;
import com.eric6166.base.exception.ErrorResponse;
import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.utils.BaseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomErrorDecoder implements ErrorDecoder {

    ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        Object rootCause = null;
        var httpStatus = HttpStatus.valueOf(response.status());
        if (response.body() != null) {
            var reader = response.body();
            var jsonNode = objectMapper.readTree(reader.asReader(StandardCharsets.UTF_8));
            var errorJsonNode = jsonNode.findValue(BaseConst.FIELD_ERROR);
            rootCause = errorJsonNode == null || errorJsonNode.isTextual() ? jsonNode : errorJsonNode;
        }
        if (rootCause == null) {
            rootCause = new ErrorResponse(httpStatus, httpStatus.name(), httpStatus.getReasonPhrase(),
                    BaseUtils.getPathFromUrl(response.request().url()), response.reason());
        }
        if (httpStatus.is5xxServerError()) {
            return new AppInternalServiceException(rootCause);
        }
        return new AppBadRequestException(rootCause);
    }


}