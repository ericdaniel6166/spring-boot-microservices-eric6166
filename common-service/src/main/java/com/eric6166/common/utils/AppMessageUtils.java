package com.eric6166.common.utils;

import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppMessageUtils {

    MessageSource messageSource;

    public void addViolation(ConstraintValidatorContext constraintValidatorContext, String messageCode, String[] messageParams) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        String message = messageSource.getMessage(messageCode, messageParams, LocaleContextHolder.getLocale());
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
