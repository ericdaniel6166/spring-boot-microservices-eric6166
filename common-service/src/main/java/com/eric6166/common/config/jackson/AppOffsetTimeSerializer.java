package com.eric6166.common.config.jackson;

import com.eric6166.base.utils.DateTimeUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

public class AppOffsetTimeSerializer extends JsonSerializer<OffsetTime> {

    @Override
    public void serialize(OffsetTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            return;
        }
        gen.writeString(DateTimeUtils.toString(value, DateTimeUtils.DEFAULT_OFFSET_TIME_FORMATTER));
    }
}