package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "weekly_metrics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyMetrics extends BaseEntity {

    private LocalDate weekStartDate;

    @Builder.Default
    private Integer totalTrips = 0;

    @Builder.Default
    private Integer completedTrips = 0;

    @Builder.Default
    private Double revenue = 0.0;
}
