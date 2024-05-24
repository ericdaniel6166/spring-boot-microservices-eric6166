package com.eric6166.base.config;

import com.eric6166.base.utils.DateTimeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {
    @Bean
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(DateTimeUtils.DEFAULT_ZONE_ID_STRING));
    }

}