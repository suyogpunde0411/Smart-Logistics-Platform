package com.smartlogistics.matchingservice.exception;

import com.smartlogistics.shared.exception.BusinessException;

public class TruckUnavailableException extends BusinessException {
    public TruckUnavailableException(String message) {
        super(message);
    }
}
