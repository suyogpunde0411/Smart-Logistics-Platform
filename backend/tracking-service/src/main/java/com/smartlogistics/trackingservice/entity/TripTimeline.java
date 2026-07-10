package com.smartlogistics.trackingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trip_timelines")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripTimeline extends BaseEntity {

    @Column(nullable = false)
    private UUID tripId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(length = 50)
    private String eventType; // GPS_UPDATE, STATUS_CHANGE, CHECKPOINT_REACHED, DELAY, FUEL_REFUEL, REST_START, REST_END
}
