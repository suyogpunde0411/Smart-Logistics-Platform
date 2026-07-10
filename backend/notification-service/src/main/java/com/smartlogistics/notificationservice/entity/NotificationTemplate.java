package com.smartlogistics.notificationservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "notification_templates")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTemplate extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name; // e.g. "Welcome Email", "Password Reset", "Trip Started"

    @Column(nullable = false)
    private String titleTemplate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String bodyTemplate;

    @Column(nullable = false, length = 20)
    private String channel; // EMAIL, SMS, PUSH, IN_APP, WHATSAPP

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String type = "SYSTEM"; // MARKETING, SYSTEM, TRIP_UPDATE, SHIPMENT_UPDATE
}
