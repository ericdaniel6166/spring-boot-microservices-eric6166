package com.eric6166.keycloak.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.Optional;

@ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true")
public interface KeycloakService {

    Optional<UserRepresentation> searchUserByUsername(String username);

    Optional<UserRepresentation> searchUserByEmail(String email);

    Optional<GroupRepresentation> searchGroupByName(String name);

    Response createUser(UserRepresentation user);

}
