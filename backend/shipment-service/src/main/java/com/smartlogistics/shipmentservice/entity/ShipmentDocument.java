package com.smartlogistics.shipmentservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "shipment_documents")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentDocument extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    // INVOICE, E_WAY_BILL, DELIVERY_CHALLAN, GST_INVOICE, INSURANCE_COPY, PURCHASE_ORDER, OTHER
    @Column(nullable = false, length = 50)
    private String documentType;

    @Column(nullable = false, length = 200)
    private String documentNumber;

    @Column(nullable = false, length = 500)
    private String fileUrl;

    @Column(length = 200)
    private String fileName;

    @Column(length = 100)
    private String contentType;

    @Column
    private Long fileSizeBytes;

    @Column
    private LocalDate expiryDate;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String status = "PENDING"; // PENDING, VERIFIED, REJECTED
}
