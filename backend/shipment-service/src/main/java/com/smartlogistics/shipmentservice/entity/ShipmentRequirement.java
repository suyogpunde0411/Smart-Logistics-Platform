package com.smartlogistics.shipmentservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "shipment_requirements")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentRequirement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    // e.g. REFRIGERATION, FRAGILE_HANDLING, HAZMAT_LICENSE, TEMP_CONTROL, CRANE
    @Column(nullable = false, length = 100)
    private String requirementType;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private boolean mandatory = true;
}
