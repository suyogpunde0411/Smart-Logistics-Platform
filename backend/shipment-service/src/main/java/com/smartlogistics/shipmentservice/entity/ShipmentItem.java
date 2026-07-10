package com.smartlogistics.shipmentservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "shipment_items")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column
    private Double weight;

    @Column
    private Double volume;

    @Column(length = 100)
    private String unit;

    @Column(length = 500)
    private String description;

    @Column
    private Double value;

    @Column(length = 10)
    private String currency;
}
