package com.eric6166.security.utils;

import com.eric6166.base.utils.BaseConst;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppSecurityUtils {

    HttpServletRequest httpServletRequest;

    public static boolean hasAnyRole(String... roles) {
        Collection<String> authorities = getAuthorities();
        for (var role : roles) {
            if (authorities.contains(getRoleWithPrefix(SecurityConst.ROLE_PREFIX, role))) {
                return true;
            }
        }
        return false;
    }

    private static String getRoleWithPrefix(String prefix, String role) {
        return role.startsWith(prefix) ? role : prefix + role;
    }

    public static boolean hasRole(String role) {
        return hasAnyRole(role);
    }

    public static Collection<String> getAuthorities() {
        return getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public static String getPreferredUsername() {
        return String.valueOf(getClaim(SecurityConst.PREFERRED_USERNAME));
    }

    public static String getUsername() {
        return getAuthentication().getName();
    }

    public static String getAuthenticationName() {
        return getAuthentication().getName();
    }

    public static String getCurrentAuditor() {
        try {
            return getUsername();
        } catch (NullPointerException ignored) {
            return SecurityConst.DEFAULT_CURRENT_AUDITOR;
        }
    }

    public static String getEmail() {
        return String.valueOf(getClaim(SecurityConst.EMAIL));
    }

    public static String getFullName() {
        return String.valueOf(getClaim(SecurityConst.NAME));
    }

    public static String getFirstName() {
        return String.valueOf(getClaim(SecurityConst.GIVEN_NAME));
    }

    public static String getLastName() {
        return String.valueOf(getClaim(SecurityConst.FAMILY_NAME));
    }

    public static Boolean getEmailVerified() {
        return (Boolean) getClaim(SecurityConst.EMAIL_VERIFIED);
    }

    public static String getScope() {
        return String.valueOf(getClaim(SecurityConst.SCOPE));
    }

    public static Object getClaim(String claim) {
        return getJwt().getClaim(claim);
    }

    public static Map<String, Object> getClaims() {
        return getJwt().getClaims();
    }

    private static Jwt getJwt() {
        return (Jwt) getAuthentication().getPrincipal();
    }

    private static Map<String, Object> getHeaders() {
        return getJwt().getHeaders();
    }

    public static String getJwtId() {
        return getJwt().getId();
    }

    public static List<String> getAudience() {
        return getJwt().getAudience();
    }

    public static String getSubject() {
        return getJwt().getSubject();
    }

    public static String getTokenValue() {
        return getJwt().getTokenValue();
    }

    public static LocalDateTime getExpiresAt() {
        return LocalDateTime.ofInstant(getJwt().getExpiresAt(), BaseConst.DEFAULT_ZONE_ID);
    }

    public static LocalDateTime getIssuedAt() {
        return LocalDateTime.ofInstant(getJwt().getIssuedAt(), BaseConst.DEFAULT_ZONE_ID);
    }

    public static LocalDateTime getNotBefore() {
        try {
            return LocalDateTime.ofInstant(getJwt().getNotBefore(), BaseConst.DEFAULT_ZONE_ID);
        } catch (NullPointerException ignored) {
            return null;
        }

    }

    public static String getIssuer() {
        return getJwt().getIssuer().toString();
    }

    private static JwtAuthenticationToken getAuthentication() {
        return (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    private static WebAuthenticationDetails getDetails() {
        return (WebAuthenticationDetails) getAuthentication().getDetails();
    }

    public static String getRemoteAddress() {
        return getDetails().getRemoteAddress();
    }

    public static String getSessionId() {
        var sid = getClaim(SecurityConst.SESSION_ID);
        return sid == null ? null : sid.toString();
    }

    public String getAuthorizationHeader() {
        return httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    }

}