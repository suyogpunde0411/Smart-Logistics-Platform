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
@Table(name = "notifications")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private UUID recipientId;

    @Column(nullable = false, length = 50)
    private String type; // MARKETING, SYSTEM, TRIP_UPDATE, SHIPMENT_UPDATE

    @Column(nullable = false, length = 20)
    private String channel; // EMAIL, SMS, PUSH, IN_APP, WHATSAPP

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false, length = 20)
    private String status; // PENDING, SENT, FAILED, DLQ

    @Column(nullable = false)
    @Builder.Default
    private boolean isRead = false;

    @Column
    private LocalDateTime readAt;

    @Column(columnDefinition = "TEXT")
    private String failureReason;

    @Column(nullable = false)
    @Builder.Default
    private Integer retryCount = 0;
}
