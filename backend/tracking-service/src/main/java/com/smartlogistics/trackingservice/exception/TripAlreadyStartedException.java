package com.smartlogistics.trackingservice.exception;

public class TripAlreadyStartedException extends RuntimeException {
    public TripAlreadyStartedException(String message) {
        super(message);
    }
}
