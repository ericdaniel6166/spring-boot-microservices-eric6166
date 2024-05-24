package com.eric6166.common.config.feign;

import com.eric6166.base.exception.AppBadRequestException;
import com.eric6166.base.exception.AppException;
import com.eric6166.base.utils.BaseConst;
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
        if (httpStatus.is5xxServerError()) {
            return new AppException(httpStatus, httpStatus.name(), httpStatus.getReasonPhrase(), rootCause);
        }
        return new AppBadRequestException(rootCause);
    }
}
