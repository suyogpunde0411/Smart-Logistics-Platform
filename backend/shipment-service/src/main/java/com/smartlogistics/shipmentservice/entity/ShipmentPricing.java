package com.smartlogistics.shipmentservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "shipment_pricing")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentPricing extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false, unique = true)
    private Shipment shipment;

    @Column
    private Double baseRate;

    @Column
    private Double distanceCharge;

    @Column
    private Double weightCharge;

    @Column
    private Double insuranceCharge;

    @Column
    private Double taxAmount;

    @Column
    private Double totalAmount;

    @Column(length = 10)
    @Builder.Default
    private String currency = "INR";

    @Column(length = 50)
    @Builder.Default
    private String pricingStatus = "ESTIMATED"; // ESTIMATED, CONFIRMED, INVOICED
}
