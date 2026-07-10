package com.smartlogistics.trackingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_events")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripEvent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false, length = 100)
    private String eventType; // STARTED, PAUSED, RESUMED, DELAYED, CHECKPOINT_REACHED, COMPLETED, CANCELLED

    @Column(nullable = false)
    private LocalDateTime eventTime;

    @Column
    private String description;

    @Column
    private Double latitude;

    @Column
    private Double longitude;
}
