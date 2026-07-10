package com.smartlogistics.truckservice.entity;

import jakarta.persistence.*;
import lombok.*;
import com.smartlogistics.shared.auditing.BaseEntity;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "truck_maintenances")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckMaintenance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false)
    private Truck truck;

    @Column(nullable = false)
    private LocalDate maintenanceDate;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Double cost;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "SCHEDULED";

    @Column(length = 100)
    private String performedBy;
}
