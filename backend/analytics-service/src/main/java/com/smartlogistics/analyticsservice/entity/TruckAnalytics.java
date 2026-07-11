package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "truck_analytics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckAnalytics extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID truckId;

    private String licensePlate;

    @Builder.Default
    private Integer totalTrips = 0;

    @Builder.Default
    private Double totalDistanceKm = 0.0;

    @Builder.Default
    private Double activeHours = 0.0;

    @Builder.Default
    private Double utilizationRate = 0.0;

    @Builder.Default
    private Double fuelConsumedLiters = 0.0;

    @Builder.Default
    private Double carbonSavingsKg = 0.0;
}
