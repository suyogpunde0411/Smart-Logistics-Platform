package com.smartlogistics.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GstNumberValidator.class)
@Documented
public @interface GstNumber {
    String message() default "Invalid Indian GST number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
