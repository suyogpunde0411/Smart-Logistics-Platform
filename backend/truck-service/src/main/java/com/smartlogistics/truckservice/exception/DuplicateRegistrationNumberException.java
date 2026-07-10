package com.smartlogistics.truckservice.exception;

import com.smartlogistics.shared.exception.DuplicateResourceException;

public class DuplicateRegistrationNumberException extends DuplicateResourceException {
    public DuplicateRegistrationNumberException(String message) {
        super(message);
    }
}
