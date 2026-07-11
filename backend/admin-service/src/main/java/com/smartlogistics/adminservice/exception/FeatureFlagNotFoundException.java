package com.smartlogistics.adminservice.exception;

public class FeatureFlagNotFoundException extends RuntimeException {
    public FeatureFlagNotFoundException(String message) {
        super(message);
    }
}
