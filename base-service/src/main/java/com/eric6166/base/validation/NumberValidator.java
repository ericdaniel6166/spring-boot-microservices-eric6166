package com.eric6166.base.validation;

import com.eric6166.base.utils.BaseMessageConst;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NumberValidator implements ConstraintValidator<ValidNumber, String> {

    private final MessageSource messageSource;

    String message;
    ValidNumber.Flag flag;

    @Override
    public void initialize(ValidNumber constraintAnnotation) {
        message = constraintAnnotation.message();
        flag = constraintAnnotation.flag();
    }

    @Override
    public boolean isValid(String numberStr, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(numberStr)) {
            return true;
        }

        boolean isValid = true;

        switch (flag) {
            case CREATABLE -> isValid = NumberUtils.isCreatable(numberStr);
            case PARSEABLE -> isValid = NumberUtils.isParsable(numberStr);
            case DIGITS -> isValid = NumberUtils.isDigits(numberStr);
            case INTEGER -> isValid = NumberUtils.isParsable(numberStr) && !numberStr.contains(".");
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
            case CREATABLE, PARSEABLE -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_NUMBER,
                    null, LocaleContextHolder.getLocale());
            case DIGITS -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_NUMBER_DIGITS,
                    null, LocaleContextHolder.getLocale());
            case INTEGER -> msg = messageSource.getMessage(BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_NUMBER_INTEGER,
                    null, LocaleContextHolder.getLocale());
            default -> {
            }
        }
        constraintValidatorContext.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}