package com.eric6166.jpa.config.audit;

import com.eric6166.security.utils.AppSecurityUtils;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;


public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(AppSecurityUtils.getCurrentAuditor());
    }
}
