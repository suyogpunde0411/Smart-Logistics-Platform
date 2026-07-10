package com.smartlogistics.notificationservice.controller;

import com.smartlogistics.common.client.NotificationFeignClient.InternalNotificationRequest;
import com.smartlogistics.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/notifications")
@RequiredArgsConstructor
@Slf4j
public class InternalNotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public void sendNotification(@RequestBody InternalNotificationRequest request) {
        log.info("Received internal Feign request to send notification. Recipient: {}, Channel: {}",
                request.recipientId(), request.channel());
        notificationService.sendDirectNotification(
                request.recipientId(),
                request.type(),
                request.channel(),
                request.title(),
                request.message()
        );
    }
}
