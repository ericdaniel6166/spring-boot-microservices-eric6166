package com.eric6166.base.validation;

import com.eric6166.base.utils.DateTimeUtils;
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
            case LOCAL_DATE -> isValid = DateTimeUtils.toOptionalLocalDate(date, pattern).isPresent();
            case LOCAL_DATE_TIME -> isValid = DateTimeUtils.toOptionalLocalDateTime(date, pattern).isPresent();
            case LOCAL_TIME -> isValid = DateTimeUtils.toOptionalLocalTime(date, pattern).isPresent();
            default -> {
            }
        }
        if (!isValid && StringUtils.isBlank(message)) {
            buildConstraintViolation(constraintValidatorContext);
        }
        return isValid;
    }

    private void buildConstraintViolation(ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        var msg = StringUtils.EMPTY;
        switch (flag) {
            case LOCAL_DATE -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_DATE,
                    null, LocaleContextHolder.getLocale());
            case LOCAL_DATE_TIME -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_DATE_TIME,
                    null, LocaleContextHolder.getLocale());
            case LOCAL_TIME -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_TIME,
                    null, LocaleContextHolder.getLocale());
            default -> {
            }
        }
        constraintValidatorContext.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}