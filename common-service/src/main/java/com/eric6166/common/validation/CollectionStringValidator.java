package com.eric6166.common.validation;

import com.eric6166.common.utils.AppValidationUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CollectionStringValidator implements ConstraintValidator<ValidCollectionString, Collection<String>> {

    final AppValidationUtils appValidationUtils;

    List<String> valueList;
    boolean caseSensitive;
    String message;
    String messageCode;
    String[] messageParams;

    @Override
    public void initialize(ValidCollectionString constraintAnnotation) {
        caseSensitive = constraintAnnotation.caseSensitive();
        valueList = Arrays.asList(constraintAnnotation.values());
        message = constraintAnnotation.message();
        messageCode = constraintAnnotation.messageCode();
        messageParams = constraintAnnotation.messageParams();
    }

    @Override
    public boolean isValid(Collection<String> s, ConstraintValidatorContext constraintValidatorContext) {
        if (CollectionUtils.isEmpty(s)) {
            return true;
        }
        boolean isValid;
        if (caseSensitive) {
            isValid = valueList.containsAll(s);
        } else {
            isValid = valueList.containsAll(s.stream().map(String::toUpperCase).toList());
        }
        return appValidationUtils.handleConstrainsValidValue(constraintValidatorContext, isValid, message, messageParams, messageCode, valueList.toString());
    }

}
