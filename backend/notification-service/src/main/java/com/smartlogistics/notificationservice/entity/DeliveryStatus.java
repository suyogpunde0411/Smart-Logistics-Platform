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
@Table(name = "delivery_statuses")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryStatus extends BaseEntity {

    @Column(nullable = false)
    private UUID notificationId;

    @Column(nullable = false, length = 20)
    private String channel;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false, length = 20)
    private String status; // DELIVERED, FAILED, READ

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 100)
    private String providerMessageId;
}
