package com.eric6166.common.config;

import com.eric6166.common.utils.Const;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {
    @Bean
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(Const.DEFAULT_TIME_ZONE_ID_STRING));
    }

}