package com.eric6166.security.config;

import com.eric6166.security.utils.SecurityConst;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@ConditionalOnProperty(name = "security.enabled", havingValue = "true")
public class SecurityConfig {

    private static final String[] ROLE_CUSTOMER_URLS = {"/customer/**"};
    private static final String[] ROLE_ADMIN_URLS = {"/admin/**"};

    SecurityProps securityProps;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(securityProps.getSkipUrls()).permitAll()
                        .requestMatchers(Optional.ofNullable(securityProps.getCustomerUrls()).orElse(ROLE_CUSTOMER_URLS)).hasRole(SecurityConst.ROLE_CUSTOMER)
                        .requestMatchers(Optional.ofNullable(securityProps.getAdminUrls()).orElse(ROLE_ADMIN_URLS)).hasRole(SecurityConst.ROLE_ADMIN)
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }


}
