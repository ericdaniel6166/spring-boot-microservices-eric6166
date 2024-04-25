package com.eric6166.common.validation;

import com.eric6166.common.utils.AppValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@RequiredArgsConstructor
public class StringValidator implements ConstraintValidator<ValidString, String> {

    final AppValidationUtils appValidationUtils;

    String[] values;
    boolean caseSensitive;
    String message;
    String messageCode;
    String[] messageParams;

    @Override
    public void initialize(ValidString constraintAnnotation) {
        values = constraintAnnotation.values();
        caseSensitive = constraintAnnotation.caseSensitive();
        message = constraintAnnotation.message();
        messageCode = constraintAnnotation.messageCode();
        messageParams = constraintAnnotation.messageParams();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(s)) {
            return true;
        }
        boolean isValid;
        if (caseSensitive) {
            isValid = StringUtils.equalsAny(s, values);
        } else {
            isValid = StringUtils.equalsAnyIgnoreCase(s, values);
        }
        return appValidationUtils.handleConstrainsValidValue(constraintValidatorContext, isValid, message, messageParams, messageCode, Arrays.toString(values));
    }


}