package com.smartlogistics.trackingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_delays")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDelay extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private Double durationMinutes;

    @Column(nullable = false, length = 50)
    private String category; // TRAFFIC, WEATHER, CUSTOMS, MECHANICAL, ACCIDENT, OTHER

    @Column(nullable = false)
    private String reason;

    @Column
    private Double latitude;

    @Column
    private Double longitude;
}
