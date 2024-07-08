package com.eric6166.keycloak.config;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "keycloak-admin-client.enabled", havingValue = "true")
public class KeycloakAminClient {

    private final Keycloak keycloak;
    private final KeycloakAdminClientProps keycloakAdminClientProps;


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


    public Optional<UserRepresentation> searchUserByEmail(String email) {
        log.info("KeycloakServiceImpl.searchUserByEmail"); // comment // for local testing
        return getUsersResource().searchByEmail(email, true).stream().findFirst();

    }

    private UsersResource getUsersResource() {
        return getRealm().users();
    }


    public Optional<GroupRepresentation> searchGroupByName(String name) {
        log.info("KeycloakServiceImpl.searchGroupByName"); // comment // for local testing
        return getRealm().groups().groups(name, 0, 1).stream().findFirst();
    }


    public Response createUser(UserRepresentation user) {
        log.info("KeycloakServiceImpl.createUser"); // comment // for local testing
        return getUsersResource().create(user);
    }
}
