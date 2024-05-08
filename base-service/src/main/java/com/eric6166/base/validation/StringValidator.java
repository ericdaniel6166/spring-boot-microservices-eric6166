package com.eric6166.base.validation;

import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.utils.BaseMessageConst;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StringValidator implements ConstraintValidator<ValidString, String> {

    private final MessageSource messageSource;

    String[] values;
    boolean caseSensitive;
    String message;

    @Override
    public void initialize(ValidString constraintAnnotation) {
        values = constraintAnnotation.values();
        caseSensitive = constraintAnnotation.caseSensitive();
        message = constraintAnnotation.message();
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
        if (!isValid && StringUtils.isBlank(message)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            var message = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_VALUE,
                    new String[]{BaseConst.PLACEHOLDER_0, Arrays.toString(values)}, LocaleContextHolder.getLocale());
            constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return isValid;
    }


}