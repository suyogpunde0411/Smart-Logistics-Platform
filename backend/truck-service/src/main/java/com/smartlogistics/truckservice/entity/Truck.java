package com.smartlogistics.truckservice.entity;

import jakarta.persistence.*;
import lombok.*;
import com.smartlogistics.shared.auditing.BaseEntity;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "trucks")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Truck extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String registrationNumber;

    @Column(nullable = false)
    private UUID ownerId; // Driver or Fleet Owner ID from user-service

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, MAINTENANCE, DECOMMISSIONED

    @OneToOne(mappedBy = "truck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TruckCapacity capacity;

    @OneToOne(mappedBy = "truck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TruckAvailability availability;

    @OneToOne(mappedBy = "truck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TruckLocation location;

    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TruckDocument> documents = new ArrayList<>();

    @OneToOne(mappedBy = "truck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TruckInsurance insurance;

    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TruckMaintenance> maintenanceRecords = new ArrayList<>();

    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TruckImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TruckStatusHistory> statusHistory = new ArrayList<>();
}
