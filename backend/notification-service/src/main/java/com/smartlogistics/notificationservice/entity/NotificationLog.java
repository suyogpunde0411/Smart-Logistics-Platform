package com.smartlogistics.notificationservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_logs")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog extends BaseEntity {

    @Column(nullable = false)
    private UUID notificationId;

    @Column(nullable = false)
    private UUID recipientId;

    @Column(nullable = false, length = 20)
    private String channel;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}
