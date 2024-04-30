package com.eric6166.common.config.feign;

import com.eric6166.common.exception.AppException;
import com.eric6166.common.utils.Const;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.nio.charset.Charset;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomErrorDecoder implements ErrorDecoder {

    ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        Object rootCause = null;
        if (response.body() != null) {
            var reader = response.body();
            var jsonNode = objectMapper.readTree(reader.asReader(Charset.defaultCharset()));
            var errorJsonNode = jsonNode.findValue(Const.FIELD_ERROR);
            rootCause = errorJsonNode == null ? jsonNode : errorJsonNode;
        } else {

        }

        var httpStatus = HttpStatus.valueOf(response.status());
//        throw new FeignException()
        return new AppException(httpStatus, httpStatus.name(), response.reason(), rootCause);
    }
}
