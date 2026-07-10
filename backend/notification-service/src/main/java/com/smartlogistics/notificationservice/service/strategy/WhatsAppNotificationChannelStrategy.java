package com.smartlogistics.notificationservice.service.strategy;

import com.smartlogistics.notificationservice.entity.Notification;
import com.smartlogistics.notificationservice.exception.InvalidChannelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WhatsAppNotificationChannelStrategy implements NotificationChannelStrategy {

    @Override
    public String getChannel() {
        return "WHATSAPP";
    }

    @Override
    public void send(Notification notification) {
        log.warn("WhatsApp integration is currently not implemented (interface-only placeholder). Notification ID: {}", notification.getId());
        throw new InvalidChannelException("WhatsApp integration is not supported in the current environment.");
    }
}
