package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "dashboard_metrics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardMetric extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String metricKey;

    @Column(nullable = false)
    private Double metricValue;

    private LocalDateTime lastUpdated;
}
