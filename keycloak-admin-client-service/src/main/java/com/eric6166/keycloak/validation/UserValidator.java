package com.eric6166.keycloak.validation;

import com.eric6166.keycloak.config.KeycloakAminClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidator {

    private final KeycloakAminClient keycloakAminClient;


    public boolean isUsernameExisted(String username) {
        log.info("UserValidation.isUsernameExisted"); // comment // for local testing
        return keycloakAminClient.searchUserByUsername(username).isPresent();
    }

    public boolean isEmailExisted(String email) {
        log.info("UserValidation.isEmailExisted"); // comment // for local testing
        return keycloakAminClient.searchUserByEmail(email).isPresent();

    }


}
