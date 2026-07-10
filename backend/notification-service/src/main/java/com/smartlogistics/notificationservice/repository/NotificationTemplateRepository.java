package com.smartlogistics.notificationservice.repository;

import com.smartlogistics.notificationservice.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, UUID> {
    Optional<NotificationTemplate> findByNameAndIsDeletedFalse(String name);
    Optional<NotificationTemplate> findByIdAndIsDeletedFalse(UUID id);
}
