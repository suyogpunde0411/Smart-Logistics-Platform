package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "fleet_analytics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FleetAnalytics extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID fleetOwnerId;

    @Builder.Default
    private Integer totalTrucks = 0;

    @Builder.Default
    private Integer totalTrips = 0;

    @Builder.Default
    private Integer activeTrucks = 0;

    @Builder.Default
    private Double utilizationRate = 0.0;

    @Builder.Default
    private Integer emptyTripsCount = 0;

    @Builder.Default
    private Double emptyDistanceKm = 0.0;

    @Builder.Default
    private Double totalDistanceKm = 0.0;

    @Builder.Default
    private Double emptyTripReductionPercent = 0.0;
}
