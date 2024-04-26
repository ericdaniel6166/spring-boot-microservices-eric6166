package com.eric6166.security.utils;

public final class SecurityConst {

    public static final String GROUP_CUSTOMER = "customer";

    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String SECURITY_REQUIREMENT_ACCESS_TOKEN = "access_token";

    public static final String PREFERRED_USERNAME = "preferred_username";
    public static final String EMAIL = "email";
    public static final String SCOPE = "scope";
    public static final String EMAIL_VERIFIED = "email_verified";
    public static final String NAME = "name";
    public static final String FAMILY_NAME = "family_name";
    public static final String GIVEN_NAME = "given_name";
    public static final String REALM_ACCESS = "realm_access";

    public static final String ROLE_PREFIX = "ROLE_";

    public static final String ROLES = "roles";

    public static final String DEFAULT_CURRENT_AUDITOR = "SYSTEM";

    private SecurityConst() {
        throw new IllegalStateException("Utility class");
    }
}
