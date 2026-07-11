package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "carbon_savings")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonSavings extends BaseEntity {

    private UUID entityId;

    private String entityType; // DRIVER, TRUCK, FLEET, PLATFORM

    @Builder.Default
    private Double co2SavedKg = 0.0;
}
