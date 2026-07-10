package com.smartlogistics.matchingservice.exception;

import com.smartlogistics.shared.exception.ResourceNotFoundException;

public class BidNotFoundException extends ResourceNotFoundException {
    public BidNotFoundException(String message) {
        super(message);
    }
}
