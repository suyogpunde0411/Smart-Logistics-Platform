package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "monthly_metrics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyMetrics extends BaseEntity {

    private Integer month;

    private Integer year;

    @Builder.Default
    private Integer totalTrips = 0;

    @Builder.Default
    private Integer completedTrips = 0;

    @Builder.Default
    private Double revenue = 0.0;
}
