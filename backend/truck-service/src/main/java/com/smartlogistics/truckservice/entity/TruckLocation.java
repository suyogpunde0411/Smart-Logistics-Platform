package com.smartlogistics.truckservice.entity;

import jakarta.persistence.*;
import lombok.*;
import com.smartlogistics.shared.auditing.BaseEntity;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "truck_locations")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckLocation extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false, unique = true)
    private Truck truck;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Double speed;

    private Double heading;

    private Double accuracy;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
