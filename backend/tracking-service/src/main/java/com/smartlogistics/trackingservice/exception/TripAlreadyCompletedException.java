package com.smartlogistics.trackingservice.exception;

public class TripAlreadyCompletedException extends RuntimeException {
    public TripAlreadyCompletedException(String message) {
        super(message);
    }
}
