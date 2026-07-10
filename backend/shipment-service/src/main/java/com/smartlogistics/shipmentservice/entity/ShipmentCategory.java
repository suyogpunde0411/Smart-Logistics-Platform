package com.smartlogistics.shipmentservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "shipment_categories")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentCategory extends BaseEntity {

    // GENERAL_CARGO, FRAGILE, PERISHABLE, HAZARDOUS, LIQUID, CONTAINER,
    // HEAVY_EQUIPMENT, FURNITURE, CONSTRUCTION_MATERIAL, AGRICULTURE_PRODUCTS
    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, length = 200)
    private String displayName;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column
    private Boolean requiresSpecialHandling;

    @Column
    private Boolean requiresRefrigeration;

    @Column
    private Boolean isHazardous;
}
