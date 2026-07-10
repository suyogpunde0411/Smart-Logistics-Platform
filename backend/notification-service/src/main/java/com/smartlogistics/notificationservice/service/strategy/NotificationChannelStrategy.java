package com.smartlogistics.notificationservice.service.strategy;

import com.smartlogistics.notificationservice.entity.Notification;

public interface NotificationChannelStrategy {
    String getChannel();
    void send(Notification notification);
}
