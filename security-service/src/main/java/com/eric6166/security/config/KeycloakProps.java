package com.eric6166.security.config;

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
@ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProps {

    String authServerUrl;
    String realm;
    String resource;
    Credentials credentials = new Credentials();

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class Credentials {
        String secret;
    }
}
