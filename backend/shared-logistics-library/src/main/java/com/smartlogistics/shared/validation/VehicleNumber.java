package com.smartlogistics.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VehicleNumberValidator.class)
@Documented
public @interface VehicleNumber {
    String message() default "Invalid vehicle registration number format (e.g. MH12AB1234)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
