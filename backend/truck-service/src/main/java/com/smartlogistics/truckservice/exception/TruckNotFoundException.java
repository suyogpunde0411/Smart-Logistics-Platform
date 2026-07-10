package com.smartlogistics.truckservice.exception;

import com.smartlogistics.shared.exception.ResourceNotFoundException;

public class TruckNotFoundException extends ResourceNotFoundException {
    public TruckNotFoundException(String message) {
        super(message);
    }
}
