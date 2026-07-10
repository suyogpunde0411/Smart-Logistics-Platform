package com.smartlogistics.notificationservice.service;

import com.smartlogistics.common.client.UserFeignClient;
import com.smartlogistics.notificationservice.dto.NotificationDto;
import com.smartlogistics.notificationservice.entity.*;
import com.smartlogistics.notificationservice.exception.InvalidChannelException;
import com.smartlogistics.notificationservice.exception.NotificationNotFoundException;
import com.smartlogistics.notificationservice.mapper.NotificationMapper;
import com.smartlogistics.notificationservice.repository.*;
import com.smartlogistics.notificationservice.service.strategy.NotificationChannelStrategy;
import com.smartlogistics.notificationservice.events.NotificationKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationLogRepository logRepository;
    private final NotificationFailureRepository failureRepository;
    private final EmailQueueRepository emailQueueRepository;
    private final SmsQueueRepository smsQueueRepository;
    private final PushQueueRepository pushQueueRepository;
    private final NotificationPreferenceService preferenceService;
    private final NotificationMapper mapper;
    private final NotificationKafkaProducer kafkaProducer;
    private final UserFeignClient userClient;

    private final Map<String, NotificationChannelStrategy> strategies;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            NotificationLogRepository logRepository,
            NotificationFailureRepository failureRepository,
            EmailQueueRepository emailQueueRepository,
            SmsQueueRepository smsQueueRepository,
            PushQueueRepository pushQueueRepository,
            NotificationPreferenceService preferenceService,
            NotificationMapper mapper,
            NotificationKafkaProducer kafkaProducer,
            UserFeignClient userClient,
            List<NotificationChannelStrategy> strategyList) {
        
        this.notificationRepository = notificationRepository;
        this.logRepository = logRepository;
        this.failureRepository = failureRepository;
        this.emailQueueRepository = emailQueueRepository;
        this.smsQueueRepository = smsQueueRepository;
        this.pushQueueRepository = pushQueueRepository;
        this.preferenceService = preferenceService;
        this.mapper = mapper;
        this.kafkaProducer = kafkaProducer;
        this.userClient = userClient;

        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.getChannel().toUpperCase(),
                        strategy -> strategy
                ));
    }

    @Override
    @Transactional
    public void sendDirectNotification(UUID recipientId, String type, String channel, String title, String message) {
        log.info("Preparing notification. Recipient: {}, Type: {}, Channel: {}", recipientId, type, channel);

        // 1. Verify User Preferences
        if (!preferenceService.isNotificationAllowed(recipientId, type, channel)) {
            log.info("Notification filtered out by user preference rules for recipient: {}", recipientId);
            return;
        }

        // 2. Persist Notification Entity (PENDING state)
        Notification notification = Notification.builder()
                .recipientId(recipientId)
                .type(type.toUpperCase())
                .channel(channel.toUpperCase())
                .title(title)
                .message(message)
                .status("PENDING")
                .retryCount(0)
                .build();
        
        Notification savedNotification = notificationRepository.save(notification);

        // 3. Resolve Dispatch Strategy
        NotificationChannelStrategy strategy = strategies.get(channel.toUpperCase());
        if (strategy == null) {
            log.error("Unsupported notification channel requested: {}", channel);
            savedNotification.setStatus("FAILED");
            savedNotification.setFailureReason("Unsupported channel: " + channel);
            notificationRepository.save(savedNotification);
            throw new InvalidChannelException("No strategy registered for channel: " + channel);
        }

        // 4. Try Delivery Dispatch
        try {
            strategy.send(savedNotification);
            
            // Log Success
            auditLog(savedNotification.getId(), recipientId, channel, "SENT", "Successfully delivered.", null);
            kafkaProducer.publishNotificationSent(savedNotification.getId(), recipientId, channel, type);
        } catch (Exception e) {
            log.warn("Initial dispatch failed for notification {}. Enqueueing for retry. Error: {}", savedNotification.getId(), e.getMessage());
            
            // Save Failure Audit
            NotificationFailure failure = NotificationFailure.builder()
                    .notificationId(savedNotification.getId())
                    .channel(channel.toUpperCase())
                    .errorMessage(e.getMessage())
                    .failedAt(LocalDateTime.now())
                    .retryAttempt(1)
                    .build();
            failureRepository.save(failure);

            // Queue in Outbox table based on channel type for background scheduler to retry
            enqueueFailedNotification(savedNotification, e.getMessage());

            savedNotification.setStatus("FAILED");
            savedNotification.setFailureReason(e.getMessage());
            notificationRepository.save(savedNotification);

            auditLog(savedNotification.getId(), recipientId, channel, "FAILED", "Initial dispatch failed. Queued for retry.", e.getMessage());
            kafkaProducer.publishNotificationFailed(savedNotification.getId(), recipientId, channel, type, e.getMessage());
        }
    }

    @Override
    @Transactional
    public void sendEmail(UUID recipientId, String subject, String body) {
        sendDirectNotification(recipientId, "SYSTEM", "EMAIL", subject, body);
    }

    @Override
    @Transactional
    public void sendSms(UUID recipientId, String message) {
        sendDirectNotification(recipientId, "SYSTEM", "SMS", "SMS Notification", message);
    }

    @Override
    @Transactional
    public void sendPush(UUID recipientId, String title, String body) {
        sendDirectNotification(recipientId, "SYSTEM", "PUSH", title, body);
    }

    @Override
    @Transactional
    public NotificationDto.Response createInAppNotification(NotificationDto.CreateRequest request) {
        Notification notification = Notification.builder()
                .recipientId(request.recipientId())
                .type(request.type().toUpperCase())
                .channel("IN_APP")
                .title(request.title())
                .message(request.message())
                .status("SENT")
                .isRead(false)
                .build();
        
        Notification saved = notificationRepository.save(notification);
        auditLog(saved.getId(), request.recipientId(), "IN_APP", "SENT", "In-app message created.", null);
        return mapper.toResponse(saved);
    }

    @Override
    public Page<NotificationDto.Response> searchNotifications(
            UUID recipientId, String type, String status, String channel, Boolean unread,
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return notificationRepository.searchNotifications(recipientId, type, status, channel, unread, startDate, endDate, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public void markAsRead(UUID id, UUID recipientId) {
        Notification notification = notificationRepository.findByIdAndRecipientIdAndIsDeletedFalse(id, recipientId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with ID: " + id));
        if (!notification.isRead()) {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public void deleteNotification(UUID id, UUID recipientId) {
        Notification notification = notificationRepository.findByIdAndRecipientIdAndIsDeletedFalse(id, recipientId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with ID: " + id));
        notification.setDeleted(true);
        notificationRepository.save(notification);
    }

    @Override
    public long getUnreadCount(UUID recipientId) {
        return notificationRepository.countByRecipientIdAndIsReadFalseAndIsDeletedFalse(recipientId);
    }

    private void enqueueFailedNotification(Notification notification, String errMsg) {
        LocalDateTime nextRetry = LocalDateTime.now().plusSeconds(5); // initial delay of 5s
        
        switch (notification.getChannel().toUpperCase()) {
            case "EMAIL":
                String email = fetchEmailAddress(notification.getRecipientId());
                EmailQueue emailQueue = EmailQueue.builder()
                        .notificationId(notification.getId())
                        .recipientEmail(email)
                        .subject(notification.getTitle())
                        .body(notification.getMessage())
                        .status("PENDING")
                        .nextRetryTime(nextRetry)
                        .retryCount(0)
                        .errorMessage(errMsg)
                        .build();
                emailQueueRepository.save(emailQueue);
                break;

            case "SMS":
                String phone = fetchPhoneNumber(notification.getRecipientId());
                SmsQueue smsQueue = SmsQueue.builder()
                        .notificationId(notification.getId())
                        .phoneNumber(phone)
                        .message(notification.getMessage())
                        .status("PENDING")
                        .nextRetryTime(nextRetry)
                        .retryCount(0)
                        .errorMessage(errMsg)
                        .build();
                smsQueueRepository.save(smsQueue);
                break;

            case "PUSH":
                String token = "fcm_token_device_" + notification.getRecipientId();
                PushQueue pushQueue = PushQueue.builder()
                        .notificationId(notification.getId())
                        .deviceToken(token)
                        .title(notification.getTitle())
                        .body(notification.getMessage())
                        .status("PENDING")
                        .nextRetryTime(nextRetry)
                        .retryCount(0)
                        .errorMessage(errMsg)
                        .build();
                pushQueueRepository.save(pushQueue);
                break;

            default:
                log.error("Outbox retry queue not supported for channel: {}", notification.getChannel());
        }
    }

    private String fetchEmailAddress(UUID recipientId) {
        try {
            UserFeignClient.InternalUserResponse user = userClient.getUser(recipientId);
            if (user != null && user.email() != null) return user.email();
        } catch (Exception e) {
            log.warn("Feign user lookup failed for email retry queuing: {}", e.getMessage());
        }
        return recipientId.toString() + "@example.com";
    }

    private String fetchPhoneNumber(UUID recipientId) {
        try {
            UserFeignClient.InternalUserResponse user = userClient.getUser(recipientId);
            if (user != null && user.phone() != null) return user.phone();
        } catch (Exception e) {
            log.warn("Feign user lookup failed for phone retry queuing: {}", e.getMessage());
        }
        return "+15555555555";
    }

    private void auditLog(UUID notificationId, UUID recipientId, String channel, String status, String details, String error) {
        NotificationLog logEntry = NotificationLog.builder()
                .notificationId(notificationId)
                .recipientId(recipientId)
                .channel(channel)
                .status(status)
                .sentAt(LocalDateTime.now())
                .details(details)
                .errorMessage(error)
                .build();
        logRepository.save(logEntry);
    }
}
