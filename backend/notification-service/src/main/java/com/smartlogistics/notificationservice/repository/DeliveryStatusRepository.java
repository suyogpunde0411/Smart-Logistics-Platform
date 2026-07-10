package com.smartlogistics.notificationservice.repository;

import com.smartlogistics.notificationservice.entity.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, UUID> {
    List<DeliveryStatus> findByNotificationIdAndIsDeletedFalse(UUID notificationId);
}
