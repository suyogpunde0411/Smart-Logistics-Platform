package com.smartlogistics.notificationservice.exception;

public class DeliveryFailedException extends RuntimeException {
    public DeliveryFailedException(String message) {
        super(message);
    }
}
