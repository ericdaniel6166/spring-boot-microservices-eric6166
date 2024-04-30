package com.eric6166.keycloak.validation.impl;

import com.eric6166.base.dto.AccountDto;
import com.eric6166.base.exception.AppValidationException;
import com.eric6166.base.exception.ValidationErrorDetail;
import com.eric6166.base.utils.BaseMessageConstant;
import com.eric6166.base.utils.Const;
import com.eric6166.keycloak.service.KeycloakService;
import com.eric6166.keycloak.validation.UserValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserValidationImpl implements UserValidation {


    MessageSource messageSource;
    KeycloakService keycloakService;

    @Override
    public void validateAccountExisted(AccountDto account) throws AppValidationException {
        log.debug("UserValidationImpl.validateAccountExisted"); // comment // for local testing
        Set<ValidationErrorDetail> errorDetails = new HashSet<>();
        Optional<UserRepresentation> searchByUsername = keycloakService.searchUserByUsername(account.getUsername());
        if (searchByUsername.isPresent()) {
            String res = messageSource.getMessage(BaseMessageConstant.MGS_RES_USERNAME, null, LocaleContextHolder.getLocale());
            String msg = messageSource.getMessage(BaseMessageConstant.MSG_ERR_RESOURCE_EXISTED, new String[]{res}, LocaleContextHolder.getLocale());
            errorDetails.add(new ValidationErrorDetail(Const.FIELD_USERNAME, StringUtils.capitalize(msg)));
        }
        Optional<UserRepresentation> searchByEmail = keycloakService.searchUserByEmail(account.getEmail());
        if (searchByEmail.isPresent()) {
            String res = messageSource.getMessage(BaseMessageConstant.MGS_RES_EMAIL, null, LocaleContextHolder.getLocale());
            String msg = messageSource.getMessage(BaseMessageConstant.MSG_ERR_RESOURCE_EXISTED, new String[]{res}, LocaleContextHolder.getLocale());
            errorDetails.add(new ValidationErrorDetail(Const.FIELD_EMAIL, StringUtils.capitalize(msg)));
        }

        if (CollectionUtils.isNotEmpty(errorDetails)) {
            throw new AppValidationException(new ArrayList<>(errorDetails));
        }


    }
}
