package com.eric6166.base.validation;

import com.eric6166.base.utils.BaseMessageConst;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {FileMaxSizeValidator.class}
)
public @interface ValidFileMaxSize {

    long maxSize() default Long.MAX_VALUE; // MB

    String message() default "{constraints.ValidFileMaxSize.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}