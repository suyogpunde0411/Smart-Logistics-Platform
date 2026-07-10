package com.smartlogistics.matchingservice.exception;

import com.smartlogistics.shared.exception.ResourceNotFoundException;

public class MatchNotFoundException extends ResourceNotFoundException {
    public MatchNotFoundException(String message) {
        super(message);
    }
}
