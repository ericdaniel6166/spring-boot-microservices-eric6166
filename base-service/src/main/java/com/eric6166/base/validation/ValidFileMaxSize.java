package com.eric6166.base.validation;

import com.eric6166.base.utils.BaseMessageConst;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {FileMaxSizeValidator.class}
)
public @interface ValidFileMaxSize {

    long maxSize() default Long.MAX_VALUE; // MB

    String message() default BaseMessageConst.MSG_ERR_CONSTRAINS_VALID_FILE_MAX_SIZE_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}