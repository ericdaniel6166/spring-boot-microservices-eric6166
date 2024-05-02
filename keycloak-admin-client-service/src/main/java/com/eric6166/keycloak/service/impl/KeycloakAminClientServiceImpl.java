package com.eric6166.keycloak.service.impl;

import com.eric6166.keycloak.config.KeycloakAdminClientProps;
import com.eric6166.keycloak.service.KeycloakAminClientService;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "keycloak-admin-client.enabled", havingValue = "true")
public class KeycloakAminClientServiceImpl implements KeycloakAminClientService {

    Keycloak keycloak;
    KeycloakAdminClientProps keycloakAdminClientProps;

    @Override
    public Optional<UserRepresentation> searchUserByUsername(String username) {
        return getUsersResource().searchByUsername(username, true).stream().findFirst();
    }

    private RealmResource getRealm() {
//        RealmResource realm = keycloak.realm(keycloakAdminClientProps.getRealm());
//        realm.groups().groups("CUSTOMER", 0, 1).stream().findFirst().get();
//        realm.users().searchByEmail("customer1@customer1.com", true).stream().findFirst().get();
//        realm.users().searchByUsername("customer1", true).stream().findFirst().get();
//        realm.users().userProfile(); //null
//        realm.getAdminEvents(); //403
//        realm.getDefaultDefaultClientScopes(); //403
//        realm.getClientSessionStats(); //403
//        realm.getEvents(); //403
//        realm.users().list();
//        realm.users().searchByFirstName("customer", true); //exact
        return keycloak.realm(keycloakAdminClientProps.getRealm());
    }

    @Override
    public Optional<UserRepresentation> searchUserByEmail(String email) {
        return getUsersResource().searchByEmail(email, true).stream().findFirst();

    }

    private UsersResource getUsersResource() {
        return getRealm().users();
    }

    @Override
    public Optional<GroupRepresentation> searchGroupByName(String name) {
        log.debug("KeycloakServiceImpl.searchGroupByName"); // comment // for local testing
        return getRealm().groups().groups(name, 0, 1).stream().findFirst();
    }

    @Override
    public Response createUser(UserRepresentation user) {
        log.debug("KeycloakServiceImpl.createUser"); // comment // for local testing
        return getUsersResource().create(user);
    }
}
