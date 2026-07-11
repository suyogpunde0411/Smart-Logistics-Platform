package com.smartlogistics.adminservice.exception;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(String message) {
        super(message);
    }
}
