package com.eric6166.common.validation;

import com.eric6166.common.utils.AppValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class EnumStringValidator implements ConstraintValidator<ValidEnumString, String> {

    final AppValidationUtils appValidationUtils;

    List<String> valueList;
    boolean caseSensitive;
    String message;
    String messageCode;
    String[] messageParams;

    @Override
    public void initialize(ValidEnumString constraintAnnotation) {
        valueList = Stream.of(constraintAnnotation.value().getEnumConstants())
                .map(Enum::name)
                .toList();
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
            isValid = valueList.contains(s);
        } else {
            isValid = valueList.contains(s.toUpperCase());
        }
        return appValidationUtils.handleConstrainsValidValue(constraintValidatorContext, isValid, message, messageParams, messageCode, valueList);
    }


}