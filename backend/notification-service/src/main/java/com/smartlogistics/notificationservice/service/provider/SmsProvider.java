package com.smartlogistics.notificationservice.service.provider;

public interface SmsProvider {
    void sendSms(String phoneNumber, String message);
}
