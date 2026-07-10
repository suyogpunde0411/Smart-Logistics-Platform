package com.smartlogistics.truckservice.exception;

import com.smartlogistics.shared.exception.BusinessException;

public class InsuranceExpiredException extends BusinessException {
    public InsuranceExpiredException(String message) {
        super(message);
    }
}
