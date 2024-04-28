package com.eric6166.jpa.config.flyway;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "spring.flyway")
public class FlywayProps {

    String baselineVersion;

    String locations;

    Boolean validateOnMigrate;

    String table;

    Boolean baselineOnMigrate;

}
