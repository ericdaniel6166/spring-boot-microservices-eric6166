package com.eric6166.keycloak.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "keycloak-admin-client.enabled", havingValue = "true")
public class KeycloakAdminClientConfig {

    private final KeycloakAdminClientProps keycloakAdminClientProps;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .serverUrl(keycloakAdminClientProps.getServerUrl())
                .realm(keycloakAdminClientProps.getRealm())
                .clientId(keycloakAdminClientProps.getClientId())
                .clientSecret(keycloakAdminClientProps.getClientSecret())
                .build();
    }
}

