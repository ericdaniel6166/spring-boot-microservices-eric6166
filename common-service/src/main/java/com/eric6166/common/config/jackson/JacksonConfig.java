package com.eric6166.common.config.jackson;

import com.eric6166.base.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtils.DEFAULT_LOCAL_DATE_TIME_PATTERN));

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeUtils.DEFAULT_LOCAL_DATE_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeUtils.DEFAULT_LOCAL_DATE_TIME_FORMATTER));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeUtils.DEFAULT_LOCAL_DATE_FORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeUtils.DEFAULT_LOCAL_DATE_FORMATTER));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeUtils.DEFAULT_LOCAL_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeUtils.DEFAULT_LOCAL_TIME_FORMATTER));

        objectMapper.registerModule(javaTimeModule);

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BigDecimal.class, new BigDecimalSerializer());

        simpleModule.addSerializer(ZonedDateTime.class, new AppZonedDateTimeSerializer());
        simpleModule.addDeserializer(ZonedDateTime.class, new AppZonedDateTimeDeserializer());
        simpleModule.addSerializer(Instant.class, new AppInstantSerializer());
        simpleModule.addDeserializer(Instant.class, new AppInstantDeserializer());
        simpleModule.addSerializer(OffsetDateTime.class, new AppOffsetDateTimeSerializer());
        simpleModule.addDeserializer(OffsetDateTime.class, new AppOffsetDateTimeDeserializer());
        simpleModule.addSerializer(OffsetTime.class, new AppOffsetTimeSerializer());
        simpleModule.addDeserializer(OffsetTime.class, new AppOffsetTimeDeserializer());

        objectMapper.registerModule(simpleModule);

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false);
        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        return objectMapper;
    }

}
