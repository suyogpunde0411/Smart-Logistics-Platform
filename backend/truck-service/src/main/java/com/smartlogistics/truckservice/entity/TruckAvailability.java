package com.smartlogistics.truckservice.entity;

import jakarta.persistence.*;
import lombok.*;
import com.smartlogistics.shared.auditing.BaseEntity;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "truck_availabilities")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckAvailability extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false, unique = true)
    private Truck truck;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "AVAILABLE";

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}
