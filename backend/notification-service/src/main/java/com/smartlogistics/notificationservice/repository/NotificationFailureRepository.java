package com.smartlogistics.notificationservice.repository;

import com.smartlogistics.notificationservice.entity.NotificationFailure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationFailureRepository extends JpaRepository<NotificationFailure, UUID> {
    List<NotificationFailure> findByNotificationIdAndIsDeletedFalse(UUID notificationId);
}
