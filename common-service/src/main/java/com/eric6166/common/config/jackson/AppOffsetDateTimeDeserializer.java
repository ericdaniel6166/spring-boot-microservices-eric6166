package com.eric6166.common.config.jackson;

import com.eric6166.base.utils.DateTimeUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;

public class AppOffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return DateTimeUtils.toOffsetDateTime(jsonParser.getText(), DateTimeUtils.DEFAULT_OFFSET_DATE_TIME_FORMATTER);
    }
}