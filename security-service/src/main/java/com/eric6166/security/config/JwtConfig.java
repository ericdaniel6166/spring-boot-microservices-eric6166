package com.eric6166.security.config;

import com.eric6166.security.utils.SecurityConst;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnProperty(name = "security.enabled", havingValue = "true")
public class JwtConfig {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverterForKeycloak() {
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            Map<String, Collection<String>> realmAccess = jwt.getClaim(SecurityConst.REALM_ACCESS);
            Collection<String> roles = realmAccess.get(SecurityConst.ROLES);
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority(SecurityConst.ROLE_PREFIX + role))
                    .collect(Collectors.toList());
        };

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        jwtAuthenticationConverter.setPrincipalClaimName(SecurityConst.PREFERRED_USERNAME);

        return jwtAuthenticationConverter;
    }
}
