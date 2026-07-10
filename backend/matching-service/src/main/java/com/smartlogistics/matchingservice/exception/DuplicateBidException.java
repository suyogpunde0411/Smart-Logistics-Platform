package com.smartlogistics.matchingservice.exception;

import com.smartlogistics.shared.exception.DuplicateResourceException;

public class DuplicateBidException extends DuplicateResourceException {
    public DuplicateBidException(String message) {
        super(message);
    }
}
