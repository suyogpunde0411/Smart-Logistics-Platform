package com.smartlogistics.notificationservice.service.strategy;

import com.smartlogistics.notificationservice.entity.Notification;
import com.smartlogistics.notificationservice.exception.DeliveryFailedException;
import com.smartlogistics.notificationservice.repository.NotificationRepository;
import com.smartlogistics.notificationservice.service.provider.PushProvider;
import com.smartlogistics.notificationservice.service.provider.PushProviderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PushNotificationChannelStrategy implements NotificationChannelStrategy {

    private final PushProviderFactory pushProviderFactory;
    private final NotificationRepository notificationRepository;

    @Override
    public String getChannel() {
        return "PUSH";
    }

    @Override
    public void send(Notification notification) {
        log.info("Processing Push notification ID: {}", notification.getId());
        
        // Simulating device token retrieval. Under a production environment, 
        // user metadata or preference attributes can capture valid tokens.
        String deviceToken = "fcm_token_device_" + notification.getRecipientId();

        try {
            PushProvider provider = pushProviderFactory.getProvider("FCM");
            provider.sendPush(deviceToken, notification.getTitle(), notification.getMessage());

            notification.setStatus("SENT");
            notificationRepository.save(notification);
            log.info("Push notification successfully sent to device token: {}", deviceToken);
        } catch (Exception e) {
            log.error("Push sending failed for user ID: {} token: {} due to: {}", 
                    notification.getRecipientId(), deviceToken, e.getMessage());
            throw new DeliveryFailedException("Push Provider Error: " + e.getMessage());
        }
    }
}
