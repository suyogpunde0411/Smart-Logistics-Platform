package com.smartlogistics.truckservice.entity;

import jakarta.persistence.*;
import lombok.*;
import com.smartlogistics.shared.auditing.BaseEntity;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "truck_capacities")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckCapacity extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false, unique = true)
    private Truck truck;

    @Column(nullable = false)
    private Double maxWeight;

    @Column(nullable = false)
    private Double maxVolume;
}
