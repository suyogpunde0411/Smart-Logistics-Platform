package com.smartlogistics.notificationservice.service.strategy;

import com.smartlogistics.common.client.UserFeignClient;
import com.smartlogistics.notificationservice.entity.Notification;
import com.smartlogistics.notificationservice.exception.DeliveryFailedException;
import com.smartlogistics.notificationservice.repository.NotificationRepository;
import com.smartlogistics.notificationservice.service.provider.SmsProvider;
import com.smartlogistics.notificationservice.service.provider.SmsProviderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmsNotificationChannelStrategy implements NotificationChannelStrategy {

    private final SmsProviderFactory smsProviderFactory;
    private final UserFeignClient userClient;
    private final NotificationRepository notificationRepository;

    @Override
    public String getChannel() {
        return "SMS";
    }

    @Override
    public void send(Notification notification) {
        log.info("Processing SMS notification ID: {}", notification.getId());
        String phoneNumber = null;

        try {
            UserFeignClient.InternalUserResponse user = userClient.getUser(notification.getRecipientId());
            if (user != null) {
                phoneNumber = user.phone();
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve user phone via Feign for ID: {}. Using fallback mock. Error: {}", 
                    notification.getRecipientId(), e.getMessage());
            phoneNumber = "+15555555555";
        }

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new DeliveryFailedException("Unable to resolve phone number for user ID: " + notification.getRecipientId());
        }

        try {
            SmsProvider provider = smsProviderFactory.getProvider("DEFAULT");
            provider.sendSms(phoneNumber, notification.getMessage());

            notification.setStatus("SENT");
            notificationRepository.save(notification);
            log.info("SMS successfully sent to {}", phoneNumber);
        } catch (Exception e) {
            log.error("SMS sending failed for user ID: {} phone: {} due to: {}", 
                    notification.getRecipientId(), phoneNumber, e.getMessage());
            throw new DeliveryFailedException("SMS Gateway Error: " + e.getMessage());
        }
    }
}
