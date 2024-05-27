package com.eric6166.base.config;

import com.eric6166.base.utils.DateTimeUtils;
import org.apache.tika.Tika;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.TimeZone;

@Configuration
public class BaseConfig {

    // mime types validator
    @Bean
    public Tika tika() {
        return new Tika();
    }

    //resource config
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/model",
                "classpath:/messages/common",
                "classpath:/messages/system",
                "classpath:/messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    //time zone config
    @Bean
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(DateTimeUtils.DEFAULT_ZONE_ID));
    }

}
