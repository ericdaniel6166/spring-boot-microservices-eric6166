package com.eric6166.base.validation;

import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.utils.BaseMessageConst;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
public class StringValidator implements ConstraintValidator<ValidString, String> {

    final MessageSource messageSource;

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
        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            var message = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_VALUE,
                    new String[]{BaseConst.PLACEHOLDER_0, Arrays.toString(values)}, LocaleContextHolder.getLocale());
            constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return isValid;
    }


}