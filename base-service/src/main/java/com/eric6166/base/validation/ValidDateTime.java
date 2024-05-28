package com.eric6166.base.validation;

import com.eric6166.base.enums.AppDateTimeFormatter;
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
        validatedBy = {DateTimeValidator.class}
)
public @interface ValidDateTime {

    Flag flag() default Flag.NONE;

    AppDateTimeFormatter formatter() default AppDateTimeFormatter.NONE;

    String pattern() default "";

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum Flag {
        NONE,
        LOCAL_DATE,
        LOCAL_DATE_TIME,
        LOCAL_TIME,
        ZONED_DATE_TIME

    }

}
