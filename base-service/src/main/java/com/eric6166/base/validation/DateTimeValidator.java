package com.eric6166.base.validation;

import com.eric6166.base.utils.AppDateUtils;
import com.eric6166.base.utils.BaseMessageConst;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DateTimeValidator implements ConstraintValidator<ValidDateTime, String> {

    private final MessageSource messageSource;

    String pattern;
    String message;
    ValidDateTime.Flag flag;

    @Override
    public void initialize(ValidDateTime constraintAnnotation) {
        pattern = constraintAnnotation.pattern();
        flag = constraintAnnotation.flag();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(date)) {
            return true;
        }
        boolean isValid = true;
        switch (flag) {
            case DATE -> isValid = AppDateUtils.toOptionalLocalDate(date, pattern).isPresent();
            case DATE_TIME -> isValid = AppDateUtils.toOptionalLocalDateTime(date, pattern).isPresent();
            default -> {
            }
        }
        if (!isValid && StringUtils.isBlank(message)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            var msg = StringUtils.EMPTY;
            switch (flag) {
                case DATE -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_DATE,
                        null, LocaleContextHolder.getLocale());
                case DATE_TIME -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_DATE_TIME,
                        null, LocaleContextHolder.getLocale());
                default -> {
                }
            }
            constraintValidatorContext.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        }
        return AppDateUtils.toOptionalLocalDate(date, pattern).isPresent();
    }
}