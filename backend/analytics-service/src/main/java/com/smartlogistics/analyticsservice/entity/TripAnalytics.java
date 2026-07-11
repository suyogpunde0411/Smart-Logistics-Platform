package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "trip_analytics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripAnalytics extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID tripId;

    private UUID shipmentId;

    private UUID driverId;

    private UUID truckId;

    private UUID businessId;

    private String status;

    @Builder.Default
    private Double distanceKm = 0.0;

    @Builder.Default
    private Double durationMinutes = 0.0;

    @Builder.Default
    private Double fuelConsumedLiters = 0.0;

    @Builder.Default
    private Double carbonSavingsKg = 0.0;

    @Builder.Default
    private Boolean isLate = false;

    @Builder.Default
    private Boolean isEmpty = false;
}
