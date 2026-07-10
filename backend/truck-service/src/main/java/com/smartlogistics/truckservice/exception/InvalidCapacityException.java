package com.smartlogistics.truckservice.exception;

import com.smartlogistics.shared.exception.ValidationException;

public class InvalidCapacityException extends ValidationException {
    public InvalidCapacityException(String message) {
        super(message);
    }
}
