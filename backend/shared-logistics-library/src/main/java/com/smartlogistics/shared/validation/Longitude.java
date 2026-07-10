package com.smartlogistics.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LongitudeValidator.class)
@Documented
public @interface Longitude {
    String message() default "Longitude must be between -180 and 180 degrees";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
