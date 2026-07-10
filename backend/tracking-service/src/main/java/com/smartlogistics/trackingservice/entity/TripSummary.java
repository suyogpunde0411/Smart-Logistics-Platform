package com.smartlogistics.trackingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_summaries")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripSummary extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column
    private LocalDateTime startActualTime;

    @Column
    private LocalDateTime endActualTime;

    @Column
    private Double durationHours;

    @Column
    private Double totalDistanceKm;

    @Column
    private Double averageSpeedKmh;

    @Column
    private Double totalFuelVolumeLiters;

    @Column
    private Double totalFuelCost;

    @Column
    private Integer delayCount;

    @Column
    private Double totalDelayMinutes;

    @Column
    private Integer restStopCount;

    @Column
    private Double totalRestMinutes;
}
