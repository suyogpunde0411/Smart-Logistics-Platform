package com.smartlogistics.matchingservice.exception;

import com.smartlogistics.shared.exception.BusinessException;

public class ShipmentUnavailableException extends BusinessException {
    public ShipmentUnavailableException(String message) {
        super(message);
    }
}
