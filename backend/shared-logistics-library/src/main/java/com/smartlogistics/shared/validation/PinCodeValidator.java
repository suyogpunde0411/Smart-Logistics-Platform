package com.smartlogistics.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PinCodeValidator implements ConstraintValidator<PinCode, String> {
    private static final Pattern PATTERN = Pattern.compile("^[1-9][0-9]{5}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return PATTERN.matcher(value).matches();
    }
}
