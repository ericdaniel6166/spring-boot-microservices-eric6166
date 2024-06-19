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
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnumStringValidator implements ConstraintValidator<ValidEnumString, String> {

    private final MessageSource messageSource;

    String[] values;
    boolean caseSensitive;
    String message;

    @Override
    public void initialize(ValidEnumString constraintAnnotation) {
        values = Stream.of(constraintAnnotation.value().getEnumConstants())
                .map(Enum::name)
                .toArray(String[]::new);
        caseSensitive = constraintAnnotation.caseSensitive();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(s)) {
            return true;
        }
        boolean isValid = caseSensitive
                ? StringUtils.equalsAny(s, values)
                : StringUtils.equalsAnyIgnoreCase(s, values);

        if (!isValid && StringUtils.isBlank(message)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            var msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_VALUE,
                    new String[]{BaseConst.PLACEHOLDER_0, Arrays.toString(values)}, LocaleContextHolder.getLocale());
            constraintValidatorContext.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        }
        return isValid;
    }

}