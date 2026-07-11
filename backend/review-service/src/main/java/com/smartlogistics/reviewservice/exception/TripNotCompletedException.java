package com.smartlogistics.reviewservice.exception;

public class TripNotCompletedException extends RuntimeException {
    public TripNotCompletedException(String message) {
        super(message);
    }
}
