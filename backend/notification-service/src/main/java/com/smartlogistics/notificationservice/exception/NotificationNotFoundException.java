package com.smartlogistics.notificationservice.exception;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String message) {
        super(message);
    }
}
