package com.smartlogistics.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PinCodeValidator.class)
@Documented
public @interface PinCode {
    String message() default "Invalid Indian PIN code format (6 digits)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
