package com.eric6166.base.config;

import com.eric6166.base.utils.BaseConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {
    @Bean
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(BaseConst.DEFAULT_TIME_ZONE_ID_STRING));
    }

}