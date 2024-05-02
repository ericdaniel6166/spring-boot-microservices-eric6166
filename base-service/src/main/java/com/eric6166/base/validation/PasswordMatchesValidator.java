package com.eric6166.base.validation;

import com.eric6166.base.dto.PasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object instanceof PasswordDto passwordDto) {
            return StringUtils.equals(passwordDto.getPassword(), passwordDto.getConfirmPassword());
        }
        return false;
    }


}