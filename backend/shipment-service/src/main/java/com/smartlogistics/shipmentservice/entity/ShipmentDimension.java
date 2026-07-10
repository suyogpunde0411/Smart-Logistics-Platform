package com.smartlogistics.shipmentservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "shipment_dimensions")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentDimension extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false, unique = true)
    private Shipment shipment;

    @Column
    private Double lengthCm;

    @Column
    private Double widthCm;

    @Column
    private Double heightCm;

    @Column
    private Double volumeCbm;

    @Column(length = 50)
    @Builder.Default
    private String dimensionUnit = "CM";
}
