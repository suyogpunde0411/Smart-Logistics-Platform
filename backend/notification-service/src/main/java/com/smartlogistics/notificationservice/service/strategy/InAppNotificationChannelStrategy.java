package com.smartlogistics.notificationservice.service.strategy;

import com.smartlogistics.notificationservice.entity.Notification;
import com.smartlogistics.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InAppNotificationChannelStrategy implements NotificationChannelStrategy {

    private final NotificationRepository notificationRepository;

    @Override
    public String getChannel() {
        return "IN_APP";
    }

    @Override
    public void send(Notification notification) {
        log.info("Processing In-App notification ID: {}", notification.getId());
        notification.setStatus("SENT");
        notificationRepository.save(notification);
    }
}
