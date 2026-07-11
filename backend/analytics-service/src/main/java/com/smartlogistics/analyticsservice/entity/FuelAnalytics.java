package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "fuel_analytics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelAnalytics extends BaseEntity {

    private String truckType;

    private String routeKey; // Origin-Destination pair representation

    @Builder.Default
    private Double totalFuelLiters = 0.0;

    @Builder.Default
    private Double averageFuelPerKm = 0.0;

    @Builder.Default
    private Double totalDistanceKm = 0.0;
}
