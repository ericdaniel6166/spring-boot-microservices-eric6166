package com.eric6166.base.validation;

import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.utils.BaseMessageConst;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CollectionStringValidator implements ConstraintValidator<ValidCollectionString, Collection<String>> {

    final MessageSource messageSource;

    List<String> valueList;
    List<String> upperCaseValueList;
    boolean caseSensitive;
    String message;

    @Override
    public void initialize(ValidCollectionString constraintAnnotation) {
        caseSensitive = constraintAnnotation.caseSensitive();
        valueList = List.of(constraintAnnotation.values());
        upperCaseValueList = valueList.stream().map(String::toUpperCase).toList();
        message = constraintAnnotation.message();
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
            isValid = upperCaseValueList.containsAll(s.stream().map(String::toUpperCase).toList());
        }
        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            var message = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_VALUE,
                    new String[]{BaseConst.PLACEHOLDER_0, valueList.toString()}, LocaleContextHolder.getLocale());
            constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return isValid;
    }

}
