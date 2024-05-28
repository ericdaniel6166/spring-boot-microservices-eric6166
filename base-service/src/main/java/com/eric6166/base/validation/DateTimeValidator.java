package com.eric6166.base.validation;

import com.eric6166.base.enums.AppDateTimeFormatter;
import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.utils.BaseMessageConst;
import com.eric6166.base.utils.DateTimeUtils;
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
    AppDateTimeFormatter formatter;

    @Override
    public void initialize(ValidDateTime constraintAnnotation) {
        pattern = constraintAnnotation.pattern();
        flag = constraintAnnotation.flag();
        message = constraintAnnotation.message();
        formatter = constraintAnnotation.formatter();
    }

    @Override
    public boolean isValid(String dateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(dateTime)) {
            return true;
        }
        boolean isValid = true;
        if (formatter != AppDateTimeFormatter.NONE) {
            isValid = DateTimeUtils.toOptionalTemporalAccessor(dateTime, formatter.getFormatter()).isPresent();

        } else {
            switch (flag) {
                case LOCAL_DATE -> isValid = DateTimeUtils.toOptionalLocalDate(dateTime, pattern).isPresent();
                case LOCAL_DATE_TIME -> isValid = DateTimeUtils.toOptionalLocalDateTime(dateTime, pattern).isPresent();
                case LOCAL_TIME -> isValid = DateTimeUtils.toOptionalLocalTime(dateTime, pattern).isPresent();
                case ZONED_DATE_TIME -> isValid = DateTimeUtils.toOptionalZonedDateTime(dateTime, pattern).isPresent();
                default -> {
                }
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
        if (formatter != AppDateTimeFormatter.NONE) {
            switch (formatter) {
                case BASIC_ISO_DATE, ISO_LOCAL_DATE, ISO_OFFSET_DATE, ISO_DATE, ISO_ORDINAL_DATE, ISO_WEEK_DATE -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_DATE,
                        new String[]{BaseConst.PLACEHOLDER_0, formatter.name()}, LocaleContextHolder.getLocale());
                case ISO_LOCAL_DATE_TIME, ISO_OFFSET_DATE_TIME, ISO_ZONED_DATE_TIME, ISO_DATE_TIME, ISO_INSTANT, RFC_1123_DATE_TIME -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_DATE_TIME,
                        new String[]{BaseConst.PLACEHOLDER_0, formatter.name()}, LocaleContextHolder.getLocale());
                case ISO_LOCAL_TIME, ISO_OFFSET_TIME, ISO_TIME -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_TIME,
                        new String[]{BaseConst.PLACEHOLDER_0, formatter.name()}, LocaleContextHolder.getLocale());
                default -> {
                }
            }
        } else {
            switch (flag) {
                case LOCAL_DATE -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_DATE,
                        new String[]{BaseConst.PLACEHOLDER_0, pattern}, LocaleContextHolder.getLocale());
                case LOCAL_DATE_TIME, ZONED_DATE_TIME -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_DATE_TIME,
                        new String[]{BaseConst.PLACEHOLDER_0, pattern}, LocaleContextHolder.getLocale());
                case LOCAL_TIME -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_TIME,
                        new String[]{BaseConst.PLACEHOLDER_0, pattern}, LocaleContextHolder.getLocale());
                default -> {
                }
            }
        }
        constraintValidatorContext.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}