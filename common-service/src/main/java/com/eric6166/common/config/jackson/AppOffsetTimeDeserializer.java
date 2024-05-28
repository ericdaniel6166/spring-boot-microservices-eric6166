package com.eric6166.common.config.jackson;

import com.eric6166.base.utils.DateTimeUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;

public class AppOffsetTimeDeserializer extends JsonDeserializer<OffsetTime> {

    @Override
    public OffsetTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return DateTimeUtils.toOffsetTime(jsonParser.getText(), DateTimeUtils.DEFAULT_OFFSET_TIME_FORMATTER);
    }
}