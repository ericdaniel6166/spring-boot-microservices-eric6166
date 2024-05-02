package com.eric6166.base.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {PasswordMatchesValidator.class}
)
public @interface PasswordMatches {

    String message() default "{constraints.PasswordMatches.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}