package com.smartlogistics.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "notification-service", path = "/internal/notifications")
public interface NotificationFeignClient {

    @PostMapping
    void sendNotification(@RequestBody InternalNotificationRequest request);

    record InternalNotificationRequest(
            UUID recipientId,
            String type,
            String channel,
            String title,
            String message
    ) {}
}
