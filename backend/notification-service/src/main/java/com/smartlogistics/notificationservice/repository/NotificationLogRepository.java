package com.smartlogistics.notificationservice.repository;

import com.smartlogistics.notificationservice.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, UUID> {
    List<NotificationLog> findByNotificationIdAndIsDeletedFalse(UUID notificationId);
}
