package com.smartlogistics.notificationservice.service.provider;

public interface PushProvider {
    void sendPush(String deviceToken, String title, String body);
}
