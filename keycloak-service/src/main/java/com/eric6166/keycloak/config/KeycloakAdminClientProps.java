package com.eric6166.keycloak.config;

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
@ConditionalOnProperty(name = "keycloak-admin-client.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "keycloak-admin-client")
public class KeycloakAdminClientProps {

    String serverUrl;
    String realm;
    String clientId;
    String clientSecret;
}
