package com.smartlogistics.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class VehicleNumberValidator implements ConstraintValidator<VehicleNumber, String> {
    private static final Pattern PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return PATTERN.matcher(value).matches();
    }
}
