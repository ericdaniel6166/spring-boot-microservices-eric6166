package com.eric6166.jpa.config.flyway;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "spring.flyway")
public class FlywayProps {

    private String baselineVersion;

    private String locations;

    private Boolean validateOnMigrate;

    private String table;

    private Boolean baselineOnMigrate;

}
