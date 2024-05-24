package com.eric6166.keycloak.validation;

import com.eric6166.base.exception.AppValidationException;
import com.eric6166.base.exception.ValidationErrorDetail;
import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.utils.BaseMessageConst;
import com.eric6166.keycloak.config.KeycloakAminClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserValidation {

    MessageSource messageSource;
    KeycloakAminClient keycloakAminClient;

    public void validateUsernameExisted(String username) throws AppValidationException {
        log.info("UserValidationImpl.validateUsernameExisted"); // comment // for local testing
        var searchByUsername = keycloakAminClient.searchUserByUsername(username);
        if (searchByUsername.isPresent()) {
            var res = messageSource.getMessage(BaseMessageConst.MGS_RES_USERNAME, null, LocaleContextHolder.getLocale());
            var msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_RESOURCE_EXISTED, new String[]{res}, LocaleContextHolder.getLocale());
            throw new AppValidationException(Collections.singletonList(new ValidationErrorDetail(BaseConst.FIELD_USERNAME, StringUtils.capitalize(msg))));
        }
    }

    public void validateEmailExisted(String email) throws AppValidationException {
        log.info("UserValidationImpl.validateEmailExisted"); // comment // for local testing
        var searchByEmail = keycloakAminClient.searchUserByEmail(email);
        if (searchByEmail.isPresent()) {
            var res = messageSource.getMessage(BaseMessageConst.MGS_RES_EMAIL, null, LocaleContextHolder.getLocale());
            var msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_RESOURCE_EXISTED, new String[]{res}, LocaleContextHolder.getLocale());
            throw new AppValidationException(Collections.singletonList(new ValidationErrorDetail(BaseConst.FIELD_USERNAME, StringUtils.capitalize(msg))));
        }
    }


}
