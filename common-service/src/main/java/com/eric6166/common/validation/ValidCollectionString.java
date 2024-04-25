package com.eric6166.common.validation;

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
        validatedBy = {CollectionStringValidator.class}
)
public @interface ValidCollectionString {
    String[] values() default {};

    boolean caseSensitive() default true;

    String message() default "";

    String messageCode() default "";

    String[] messageParams() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
