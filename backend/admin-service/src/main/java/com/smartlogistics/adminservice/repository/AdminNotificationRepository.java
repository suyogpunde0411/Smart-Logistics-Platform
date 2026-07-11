package com.smartlogistics.adminservice.repository;

import com.smartlogistics.adminservice.entity.AdminNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface AdminNotificationRepository extends JpaRepository<AdminNotification, UUID> {
    List<AdminNotification> findByRecipientAdminIdAndIsReadFalseAndIsDeletedFalse(UUID recipientAdminId);
}
