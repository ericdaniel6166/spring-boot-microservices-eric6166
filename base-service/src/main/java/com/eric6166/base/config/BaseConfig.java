package com.eric6166.base.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {

    // mime types validator
    @Bean
    public Tika tika() {
        return new Tika();
    }

}
