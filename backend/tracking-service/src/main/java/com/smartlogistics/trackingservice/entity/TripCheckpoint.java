package com.smartlogistics.trackingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_checkpoints")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripCheckpoint extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Integer sequenceIndex;

    @Column(nullable = false, length = 50)
    private String type; // PICKUP, LOADING_STARTED, LOADING_COMPLETED, EN_ROUTE, REST_STOP, DESTINATION, UNLOADING_STARTED, UNLOADING_COMPLETED

    @Column(nullable = false, length = 50)
    private String status; // PENDING, REACHED, DEPARTED

    @Column
    private LocalDateTime plannedArrivalTime;

    @Column
    private LocalDateTime actualArrivalTime;

    @Column
    private LocalDateTime departureTime;
}
