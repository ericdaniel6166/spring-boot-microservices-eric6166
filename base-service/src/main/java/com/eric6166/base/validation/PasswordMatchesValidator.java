package com.eric6166.base.validation;

import com.eric6166.base.dto.PasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordDto> {

    @Override
    public boolean isValid(PasswordDto password, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.equals(password.getPassword(), password.getConfirmPassword());
    }


}