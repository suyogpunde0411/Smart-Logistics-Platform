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
@Table(name = "notification_failures")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationFailure extends BaseEntity {

    @Column(nullable = false)
    private UUID notificationId;

    @Column
    private UUID queueId;

    @Column(nullable = false, length = 20)
    private String channel;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime failedAt;

    @Column(nullable = false)
    private Integer retryAttempt;
}
