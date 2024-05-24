package com.eric6166.base.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {NumberValidator.class}
)
public @interface ValidNumber {

    Flag flag() default Flag.NOT_VALIDATE;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum Flag {
        NOT_VALIDATE,
        CREATABLE, // valid Java number
        PARSEABLE, // Hexadecimal and scientific notations are not considered parsable
        DIGITS, // contains only digit characters
        INTEGER,

    }
}
