package com.smartlogistics.notificationservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "notification_preferences")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreference extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false)
    @Builder.Default
    private boolean emailEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean smsEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean pushEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean marketingEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean systemAlertsEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean tripUpdatesEnabled = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean shipmentUpdatesEnabled = true;
}
