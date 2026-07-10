package com.smartlogistics.trackingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "trips")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip extends BaseEntity {

    @Column(nullable = false)
    private UUID shipmentId;

    @Column(nullable = false)
    private UUID truckId;

    @Column(nullable = false)
    private UUID driverId;

    @Column(nullable = false)
    private UUID businessId;

    @Column(nullable = false, length = 50)
    private String status; // WAITING, ASSIGNED, READY, STARTED, IN_PROGRESS, PAUSED, RESUMED, COMPLETED, CANCELLED

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime completedAt;

    @Column
    private Double totalDistanceTravelledKm;

    @Column
    private Double currentLatitude;

    @Column
    private Double currentLongitude;

    @Column
    private LocalDateTime lastGpsUpdatedAt;

    @Column
    private LocalDateTime expectedArrivalTime;

    @Column
    private Double remainingDistanceKm;

    @Column
    private Double averageSpeedKmh;

    @OneToOne(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TripRoute route;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TripCheckpoint> checkpoints = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<GpsLocation> gpsLocations = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TripEvent> events = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<FuelLog> fuelLogs = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RestStop> restStops = new ArrayList<>();

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TripDelay> delays = new ArrayList<>();

    @OneToOne(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private TripSummary summary;
}
