package com.eric6166.keycloak.validation;


import com.eric6166.base.dto.AccountDto;
import com.eric6166.base.exception.AppValidationException;

public interface UserValidation {
    void validateAccountExisted(AccountDto request) throws AppValidationException;
}
