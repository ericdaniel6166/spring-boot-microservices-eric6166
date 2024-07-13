package com.eric6166.base.validation;

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
        validatedBy = {NumberValidator.class}
)
public @interface ValidNumber {

    Flag flag() default Flag.NONE;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum Flag {
        NONE,
        CREATABLE, // valid Java number
        PARSEABLE, // Hexadecimal and scientific notations are not considered parsable
        DIGITS, // contains only digit characters
        BIG_INTEGER,

    }
}
