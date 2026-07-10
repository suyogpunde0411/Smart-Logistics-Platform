package com.smartlogistics.shipmentservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import com.smartlogistics.shared.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shipments")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String trackingNumber;

    @Column(nullable = false)
    private UUID businessOwnerId;

    // Origin & Destination stored as address strings for search
    @Column(nullable = false, length = 500)
    private String originAddress;

    @Column(nullable = false)
    private Double originLatitude;

    @Column(nullable = false)
    private Double originLongitude;

    @Column(nullable = false, length = 500)
    private String destinationAddress;

    @Column(nullable = false)
    private Double destinationLatitude;

    @Column(nullable = false)
    private Double destinationLongitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private ShipmentStatus status = ShipmentStatus.CREATED;

    @Column(nullable = false, length = 50)
    private String cargoType; // from CargoType enum

    @Column(nullable = false)
    private Double totalWeight;

    @Column
    private Double totalVolume;

    @Column(length = 1000)
    private String description;

    @Column
    private Double budgetAmount;

    @Column(length = 10)
    private String budgetCurrency;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String weightUnit = "KG";

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String volumeUnit = "CBM";

    @Column(length = 50)
    private String requiredTruckType;

    @Column
    private LocalDateTime expiresAt;

    @OneToOne(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PickupDetails pickupDetails;

    @OneToOne(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DropDetails dropDetails;

    @OneToOne(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ShipmentPricing pricing;

    @OneToOne(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ShipmentDimension dimension;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ShipmentItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ShipmentDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ShipmentImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ShipmentStatusHistory> statusHistory = new ArrayList<>();

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ShipmentRequirement> requirements = new ArrayList<>();
}
