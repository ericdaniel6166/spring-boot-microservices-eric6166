package com.eric6166.common.utils;

import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppValidationUtils {

    AppMessageUtils appMessageUtils;

    public boolean handleConstrainsValidValue(ConstraintValidatorContext constraintValidatorContext, boolean isValid, String message, String[] messageParams, String messageCode, String validValues) {
        if (!isValid && StringUtils.isBlank(message)) {
            if (messageParams.length == 0 && StringUtils.isBlank(messageCode)) {
                appMessageUtils.addViolation(constraintValidatorContext,
                        MessageConstant.MSG_ERR_CONSTRAINS_VALID_VALUE,
                        new String[]{Const.PLACEHOLDER_0, validValues});
            } else {
                appMessageUtils.addViolation(constraintValidatorContext, messageCode, messageParams);
            }
        }
        return isValid;
    }

}
