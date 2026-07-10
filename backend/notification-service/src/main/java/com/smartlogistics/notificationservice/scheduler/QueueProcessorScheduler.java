package com.smartlogistics.notificationservice.scheduler;

import com.smartlogistics.notificationservice.entity.*;
import com.smartlogistics.notificationservice.events.NotificationKafkaProducer;
import com.smartlogistics.notificationservice.repository.*;
import com.smartlogistics.notificationservice.service.strategy.NotificationChannelStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QueueProcessorScheduler {

    private final EmailQueueRepository emailQueueRepository;
    private final SmsQueueRepository smsQueueRepository;
    private final PushQueueRepository pushQueueRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationLogRepository logRepository;
    private final NotificationFailureRepository failureRepository;
    private final NotificationKafkaProducer kafkaProducer;

    private final Map<String, NotificationChannelStrategy> strategies;

    @Value("${notification.retry.max-attempts:5}")
    private int maxAttempts;

    @Value("${notification.retry.backoff-ms:1000}")
    private long baseBackoffMs;

    @Value("${notification.retry.multiplier:2.0}")
    private double backoffMultiplier;

    public QueueProcessorScheduler(
            EmailQueueRepository emailQueueRepository,
            SmsQueueRepository smsQueueRepository,
            PushQueueRepository pushQueueRepository,
            NotificationRepository notificationRepository,
            NotificationLogRepository logRepository,
            NotificationFailureRepository failureRepository,
            NotificationKafkaProducer kafkaProducer,
            List<NotificationChannelStrategy> strategyList) {
        
        this.emailQueueRepository = emailQueueRepository;
        this.smsQueueRepository = smsQueueRepository;
        this.pushQueueRepository = pushQueueRepository;
        this.notificationRepository = notificationRepository;
        this.logRepository = logRepository;
        this.failureRepository = failureRepository;
        this.kafkaProducer = kafkaProducer;

        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.getChannel().toUpperCase(),
                        strategy -> strategy
                ));
    }

    @Scheduled(fixedDelay = 10000) // Execute queue poll every 10 seconds
    @Transactional
    public void processQueues() {
        LocalDateTime now = LocalDateTime.now();
        
        processEmailQueue(now);
        processSmsQueue(now);
        processPushQueue(now);
    }

    private void processEmailQueue(LocalDateTime now) {
        List<EmailQueue> retryable = emailQueueRepository.findByStatusInAndNextRetryTimeBeforeAndIsDeletedFalse(
                List.of("PENDING", "FAILED"), now
        );

        for (EmailQueue item : retryable) {
            log.info("Retrying email dispatch for notification ID: {} (Attempt {})", item.getNotificationId(), item.getRetryCount() + 1);
            Notification notification = notificationRepository.findByIdAndIsDeletedFalse(item.getNotificationId()).orElse(null);
            
            if (notification == null) {
                item.setStatus("DLQ");
                emailQueueRepository.save(item);
                continue;
            }

            if (item.getRetryCount() >= maxAttempts) {
                moveToDlq(notification, item, "EMAIL", "Max retry attempts exceeded.");
                continue;
            }

            NotificationChannelStrategy strategy = strategies.get("EMAIL");
            try {
                // Increment attempts
                item.setRetryCount(item.getRetryCount() + 1);
                notification.setRetryCount(item.getRetryCount());
                
                strategy.send(notification);
                
                // Success
                item.setStatus("SENT");
                emailQueueRepository.save(item);

                notification.setStatus("SENT");
                notificationRepository.save(notification);

                auditLog(notification.getId(), notification.getRecipientId(), "EMAIL", "SENT", "Successfully delivered after retry.", null);
                kafkaProducer.publishNotificationSent(notification.getId(), notification.getRecipientId(), "EMAIL", notification.getType());
            } catch (Exception e) {
                handleRetryFailure(notification, item, "EMAIL", e.getMessage());
            }
        }
    }

    private void processSmsQueue(LocalDateTime now) {
        List<SmsQueue> retryable = smsQueueRepository.findByStatusInAndNextRetryTimeBeforeAndIsDeletedFalse(
                List.of("PENDING", "FAILED"), now
        );

        for (SmsQueue item : retryable) {
            log.info("Retrying SMS dispatch for notification ID: {} (Attempt {})", item.getNotificationId(), item.getRetryCount() + 1);
            Notification notification = notificationRepository.findByIdAndIsDeletedFalse(item.getNotificationId()).orElse(null);

            if (notification == null) {
                item.setStatus("DLQ");
                smsQueueRepository.save(item);
                continue;
            }

            if (item.getRetryCount() >= maxAttempts) {
                moveToDlq(notification, item, "SMS", "Max retry attempts exceeded.");
                continue;
            }

            NotificationChannelStrategy strategy = strategies.get("SMS");
            try {
                item.setRetryCount(item.getRetryCount() + 1);
                notification.setRetryCount(item.getRetryCount());

                strategy.send(notification);

                item.setStatus("SENT");
                smsQueueRepository.save(item);

                notification.setStatus("SENT");
                notificationRepository.save(notification);

                auditLog(notification.getId(), notification.getRecipientId(), "SMS", "SENT", "Successfully delivered after retry.", null);
                kafkaProducer.publishNotificationSent(notification.getId(), notification.getRecipientId(), "SMS", notification.getType());
            } catch (Exception e) {
                handleRetryFailure(notification, item, "SMS", e.getMessage());
            }
        }
    }

    private void processPushQueue(LocalDateTime now) {
        List<PushQueue> retryable = pushQueueRepository.findByStatusInAndNextRetryTimeBeforeAndIsDeletedFalse(
                List.of("PENDING", "FAILED"), now
        );

        for (PushQueue item : retryable) {
            log.info("Retrying Push dispatch for notification ID: {} (Attempt {})", item.getNotificationId(), item.getRetryCount() + 1);
            Notification notification = notificationRepository.findByIdAndIsDeletedFalse(item.getNotificationId()).orElse(null);

            if (notification == null) {
                item.setStatus("DLQ");
                pushQueueRepository.save(item);
                continue;
            }

            if (item.getRetryCount() >= maxAttempts) {
                moveToDlq(notification, item, "PUSH", "Max retry attempts exceeded.");
                continue;
            }

            NotificationChannelStrategy strategy = strategies.get("PUSH");
            try {
                item.setRetryCount(item.getRetryCount() + 1);
                notification.setRetryCount(item.getRetryCount());

                strategy.send(notification);

                item.setStatus("SENT");
                pushQueueRepository.save(item);

                notification.setStatus("SENT");
                notificationRepository.save(notification);

                auditLog(notification.getId(), notification.getRecipientId(), "PUSH", "SENT", "Successfully delivered after retry.", null);
                kafkaProducer.publishNotificationSent(notification.getId(), notification.getRecipientId(), "PUSH", notification.getType());
            } catch (Exception e) {
                handleRetryFailure(notification, item, "PUSH", e.getMessage());
            }
        }
    }

    private void handleRetryFailure(Notification notification, Object queueItem, String channel, String errMsg) {
        int currentAttempt = 0;
        LocalDateTime nextRetry = LocalDateTime.now();

        if (queueItem instanceof EmailQueue email) {
            currentAttempt = email.getRetryCount() + 1;
            email.setRetryCount(currentAttempt);
            email.setErrorMessage(errMsg);
            email.setStatus("FAILED");
            
            long backoff = calculateBackoff(currentAttempt);
            nextRetry = LocalDateTime.now().plusSeconds(backoff / 1000);
            email.setNextRetryTime(nextRetry);
            emailQueueRepository.save(email);
        } else if (queueItem instanceof SmsQueue sms) {
            currentAttempt = sms.getRetryCount() + 1;
            sms.setRetryCount(currentAttempt);
            sms.setErrorMessage(errMsg);
            sms.setStatus("FAILED");

            long backoff = calculateBackoff(currentAttempt);
            nextRetry = LocalDateTime.now().plusSeconds(backoff / 1000);
            sms.setNextRetryTime(nextRetry);
            smsQueueRepository.save(sms);
        } else if (queueItem instanceof PushQueue push) {
            currentAttempt = push.getRetryCount() + 1;
            push.setRetryCount(currentAttempt);
            push.setErrorMessage(errMsg);
            push.setStatus("FAILED");

            long backoff = calculateBackoff(currentAttempt);
            nextRetry = LocalDateTime.now().plusSeconds(backoff / 1000);
            push.setNextRetryTime(nextRetry);
            pushQueueRepository.save(push);
        }

        notification.setRetryCount(currentAttempt);
        notification.setStatus("FAILED");
        notification.setFailureReason(errMsg);
        notificationRepository.save(notification);

        // Record Failure Detail
        NotificationFailure failure = NotificationFailure.builder()
                .notificationId(notification.getId())
                .channel(channel)
                .errorMessage(errMsg)
                .failedAt(LocalDateTime.now())
                .retryAttempt(currentAttempt)
                .build();
        failureRepository.save(failure);

        auditLog(notification.getId(), notification.getRecipientId(), channel, "FAILED", 
                "Retry failed. Scheduled next attempt for: " + nextRetry, errMsg);
    }

    private void moveToDlq(Notification notification, Object queueItem, String channel, String reason) {
        log.error("Notification ID: {} has failed after maximum retries. Moving to Dead Letter Queue (DLQ).", notification.getId());

        if (queueItem instanceof EmailQueue email) {
            email.setStatus("DLQ");
            emailQueueRepository.save(email);
        } else if (queueItem instanceof SmsQueue sms) {
            sms.setStatus("DLQ");
            smsQueueRepository.save(sms);
        } else if (queueItem instanceof PushQueue push) {
            push.setStatus("DLQ");
            pushQueueRepository.save(push);
        }

        notification.setStatus("DLQ");
        notification.setFailureReason(reason);
        notificationRepository.save(notification);

        auditLog(notification.getId(), notification.getRecipientId(), channel, "DLQ", "Max retry limits reached.", reason);
        kafkaProducer.publishNotificationFailed(notification.getId(), notification.getRecipientId(), channel, notification.getType(), reason);
    }

    private long calculateBackoff(int attempt) {
        return (long) (baseBackoffMs * Math.pow(backoffMultiplier, attempt));
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
