package com.smartlogistics.matchingservice.exception;

import com.smartlogistics.shared.exception.BusinessException;

public class InvalidMatchException extends BusinessException {
    public InvalidMatchException(String message) {
        super(message);
    }
}
