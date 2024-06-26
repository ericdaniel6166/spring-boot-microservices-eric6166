package com.eric6166.jpa.config.audit;

import com.eric6166.security.utils.AppSecurityUtils;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    private static final String DEFAULT_CURRENT_AUDITOR = "SYSTEM";

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return Optional.of(AppSecurityUtils.getUsername());
        } catch (NullPointerException ignored) {
            return Optional.of(DEFAULT_CURRENT_AUDITOR);
        }
    }

}
