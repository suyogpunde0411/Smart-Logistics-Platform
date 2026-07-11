package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "business_analytics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessAnalytics extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID businessId;

    @Builder.Default
    private Integer totalShipments = 0;

    @Builder.Default
    private Integer completedShipments = 0;

    @Builder.Default
    private Integer cancelledShipments = 0;

    @Builder.Default
    private Double totalBudgetSpent = 0.0;

    @Builder.Default
    private Double averageRating = 5.0;
}
