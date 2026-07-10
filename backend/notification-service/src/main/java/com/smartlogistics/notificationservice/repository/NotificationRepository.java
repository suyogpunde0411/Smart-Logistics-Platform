package com.smartlogistics.notificationservice.repository;

import com.smartlogistics.notificationservice.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Optional<Notification> findByIdAndIsDeletedFalse(UUID id);

    Optional<Notification> findByIdAndRecipientIdAndIsDeletedFalse(UUID id, UUID recipientId);

    long countByRecipientIdAndIsReadFalseAndIsDeletedFalse(UUID recipientId);

    @Query("SELECT n FROM Notification n WHERE " +
            "n.recipientId = :recipientId AND " +
            "(:type IS NULL OR n.type = :type) AND " +
            "(:status IS NULL OR n.status = :status) AND " +
            "(:channel IS NULL OR n.channel = :channel) AND " +
            "(:unread IS NULL OR n.isRead = :unread) AND " +
            "(:startDate IS NULL OR n.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR n.createdAt <= :endDate)")
    Page<Notification> searchNotifications(
            @Param("recipientId") UUID recipientId,
            @Param("type") String type,
            @Param("status") String status,
            @Param("channel") String channel,
            @Param("unread") Boolean unread,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);
}
