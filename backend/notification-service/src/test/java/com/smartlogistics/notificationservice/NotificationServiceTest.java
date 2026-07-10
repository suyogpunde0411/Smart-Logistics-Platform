package com.smartlogistics.notificationservice;

import com.smartlogistics.common.client.UserFeignClient;
import com.smartlogistics.notificationservice.entity.EmailQueue;
import com.smartlogistics.notificationservice.entity.Notification;
import com.smartlogistics.notificationservice.events.NotificationKafkaProducer;
import com.smartlogistics.notificationservice.repository.*;
import com.smartlogistics.notificationservice.service.NotificationPreferenceService;
import com.smartlogistics.notificationservice.service.NotificationServiceImpl;
import com.smartlogistics.notificationservice.service.strategy.NotificationChannelStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock private NotificationRepository notificationRepository;
    @Mock private NotificationLogRepository logRepository;
    @Mock private NotificationFailureRepository failureRepository;
    @Mock private EmailQueueRepository emailQueueRepository;
    @Mock private SmsQueueRepository smsQueueRepository;
    @Mock private PushQueueRepository pushQueueRepository;
    @Mock private NotificationPreferenceService preferenceService;
    @Mock private NotificationKafkaProducer kafkaProducer;
    @Mock private UserFeignClient userClient;
    @Mock private NotificationChannelStrategy mockEmailStrategy;

    private NotificationServiceImpl notificationService;
    private UUID recipientId;

    @BeforeEach
    public void setUp() {
        // Setup strategy mock behaviors
        when(mockEmailStrategy.getChannel()).thenReturn("EMAIL");

        notificationService = new NotificationServiceImpl(
                notificationRepository, logRepository, failureRepository,
                emailQueueRepository, smsQueueRepository, pushQueueRepository,
                preferenceService, null, kafkaProducer, userClient,
                List.of(mockEmailStrategy)
        );

        recipientId = UUID.randomUUID();
    }

    @Test
    public void testSendDirectNotification_PreferenceDisallowed_DoesNotSend() {
        when(preferenceService.isNotificationAllowed(recipientId, "SYSTEM", "EMAIL")).thenReturn(false);

        notificationService.sendDirectNotification(recipientId, "SYSTEM", "EMAIL", "Subject", "Body");

        verifyNoInteractions(notificationRepository);
        verify(mockEmailStrategy, never()).send(any());
    }

    @Test
    public void testSendDirectNotification_PreferenceAllowed_SendsSuccessfully() {
        when(preferenceService.isNotificationAllowed(recipientId, "SYSTEM", "EMAIL")).thenReturn(true);
        
        Notification notification = Notification.builder()
                .recipientId(recipientId)
                .channel("EMAIL")
                .type("SYSTEM")
                .title("Subject")
                .message("Body")
                .status("PENDING")
                .build();
        
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        notificationService.sendDirectNotification(recipientId, "SYSTEM", "EMAIL", "Subject", "Body");

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(mockEmailStrategy, times(1)).send(any(Notification.class));
        verify(kafkaProducer, times(1)).publishNotificationSent(any(), eq(recipientId), eq("EMAIL"), eq("SYSTEM"));
    }

    @Test
    public void testSendDirectNotification_DeliveryFails_EnqueuesForRetry() {
        when(preferenceService.isNotificationAllowed(recipientId, "SYSTEM", "EMAIL")).thenReturn(true);

        Notification notification = Notification.builder()
                .recipientId(recipientId)
                .channel("EMAIL")
                .type("SYSTEM")
                .title("Subject")
                .message("Body")
                .status("PENDING")
                .build();
        
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        doThrow(new RuntimeException("SMTP Server down")).when(mockEmailStrategy).send(any(Notification.class));

        notificationService.sendDirectNotification(recipientId, "SYSTEM", "EMAIL", "Subject", "Body");

        verify(mockEmailStrategy, times(1)).send(any(Notification.class));
        verify(emailQueueRepository, times(1)).save(any(EmailQueue.class));
        verify(kafkaProducer, times(1)).publishNotificationFailed(any(), eq(recipientId), eq("EMAIL"), eq("SYSTEM"), eq("SMTP Server down"));
    }
}
