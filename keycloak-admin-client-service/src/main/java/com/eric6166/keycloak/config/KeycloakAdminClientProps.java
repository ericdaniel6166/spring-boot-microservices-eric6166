package com.eric6166.keycloak.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConditionalOnProperty(name = "keycloak-admin-client.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "keycloak-admin-client")
public class KeycloakAdminClientProps {

    private String serverUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String issuerUri;
}
