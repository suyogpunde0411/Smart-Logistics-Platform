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
@Table(name = "sms_queue")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsQueue extends BaseEntity {

    @Column(nullable = false)
    private UUID notificationId;

    @Column(nullable = false, length = 30)
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false, length = 20)
    private String status; // PENDING, SENT, FAILED, DLQ

    @Column
    private LocalDateTime nextRetryTime;

    @Column(nullable = false)
    @Builder.Default
    private Integer retryCount = 0;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}
