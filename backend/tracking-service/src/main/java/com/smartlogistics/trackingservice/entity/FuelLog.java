package com.smartlogistics.trackingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "fuel_logs")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private Double fuelVolumeLiters;

    @Column(nullable = false)
    private Double costPerLiter;

    @Column(nullable = false)
    private Double totalCost;

    @Column(nullable = false)
    private Double odometerKm;

    @Column
    private String stationName;

    @Column
    private String stationLocation;

    @Column(nullable = false)
    private LocalDateTime refueledAt;
}
