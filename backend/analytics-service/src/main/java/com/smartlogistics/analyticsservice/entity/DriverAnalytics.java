package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "driver_analytics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverAnalytics extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID driverId;

    @Builder.Default
    private Integer totalTrips = 0;

    @Builder.Default
    private Integer completedTrips = 0;

    @Builder.Default
    private Integer cancelledTrips = 0;

    @Builder.Default
    private Double averageRating = 5.0;

    @Builder.Default
    private Double totalDistanceTravelled = 0.0;

    @Builder.Default
    private Double totalDurationMinutes = 0.0;

    @Builder.Default
    private Double averageDeliveryTimeMinutes = 0.0;

    @Builder.Default
    private Double totalRevenue = 0.0;

    @Builder.Default
    private Integer lateDeliveries = 0;

    @Builder.Default
    private Integer trustScore = 100;
}
