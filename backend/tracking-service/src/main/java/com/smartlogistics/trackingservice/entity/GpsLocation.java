package com.smartlogistics.trackingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "gps_locations")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GpsLocation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    private Double speedKmh;

    @Column
    private Double heading;

    @Column
    private Double accuracy;

    @Column
    private Double altitude;

    @Column
    private Double distanceTravelledKm;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
