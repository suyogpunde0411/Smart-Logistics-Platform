package com.smartlogistics.trackingservice.exception;

public class InvalidTripStateException extends RuntimeException {
    public InvalidTripStateException(String message) {
        super(message);
    }
}
