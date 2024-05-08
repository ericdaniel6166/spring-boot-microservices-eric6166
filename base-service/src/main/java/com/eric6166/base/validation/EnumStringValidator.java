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

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnumStringValidator implements ConstraintValidator<ValidEnumString, String> {

    private final MessageSource messageSource;

    List<String> valueList;
    boolean caseSensitive;
    String message;

    @Override
    public void initialize(ValidEnumString constraintAnnotation) {
        valueList = Stream.of(constraintAnnotation.value().getEnumConstants())
                .map(Enum::name)
                .toList();
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
            isValid = valueList.contains(s);
        } else {
            isValid = valueList.contains(s.toUpperCase());
        }
        if (!isValid && StringUtils.isBlank(message)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            String message = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_VALUE,
                    new String[]{BaseConst.PLACEHOLDER_0, valueList.toString()}, LocaleContextHolder.getLocale());
            constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return isValid;
    }


}