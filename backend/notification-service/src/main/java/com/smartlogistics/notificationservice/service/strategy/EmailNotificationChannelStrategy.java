package com.smartlogistics.notificationservice.service.strategy;

import com.smartlogistics.common.client.UserFeignClient;
import com.smartlogistics.notificationservice.entity.Notification;
import com.smartlogistics.notificationservice.exception.DeliveryFailedException;
import com.smartlogistics.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationChannelStrategy implements NotificationChannelStrategy {

    private final JavaMailSender mailSender;
    private final UserFeignClient userClient;
    private final NotificationRepository notificationRepository;

    @Override
    public String getChannel() {
        return "EMAIL";
    }

    @Override
    public void send(Notification notification) {
        log.info("Processing Email notification ID: {}", notification.getId());
        String emailAddress = null;

        try {
            UserFeignClient.InternalUserResponse user = userClient.getUser(notification.getRecipientId());
            if (user != null) {
                emailAddress = user.email();
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve user email via Feign for ID: {}. Using fallback mock. Error: {}", 
                    notification.getRecipientId(), e.getMessage());
            emailAddress = notification.getRecipientId().toString() + "@example.com";
        }

        if (emailAddress == null || emailAddress.isBlank()) {
            throw new DeliveryFailedException("Unable to resolve email address for user ID: " + notification.getRecipientId());
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(emailAddress);
            mailMessage.setSubject(notification.getTitle());
            mailMessage.setText(notification.getMessage());
            mailMessage.setFrom("smartlogisticsplatform@gmail.com");

            mailSender.send(mailMessage);
            
            notification.setStatus("SENT");
            notificationRepository.save(notification);
            log.info("Email successfully sent to {}", emailAddress);
        } catch (Exception e) {
            log.error("Email sending failed for user ID: {} email: {} due to: {}", 
                    notification.getRecipientId(), emailAddress, e.getMessage());
            throw new DeliveryFailedException("SMTP Send Failed: " + e.getMessage());
        }
    }
}
