package com.smartlogistics.truckservice.exception;

import com.smartlogistics.shared.exception.ResourceNotFoundException;

public class DocumentNotFoundException extends ResourceNotFoundException {
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
