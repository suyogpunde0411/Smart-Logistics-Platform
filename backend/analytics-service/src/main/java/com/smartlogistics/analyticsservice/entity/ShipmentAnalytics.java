package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "shipment_analytics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentAnalytics extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID shipmentId;

    private UUID businessId;

    private String originAddress;

    private String destinationAddress;

    private Double totalWeight;

    private String cargoType;

    private Double budgetAmount;

    private String status;
}
