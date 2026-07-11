package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "matching_analytics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingAnalytics extends BaseEntity {

    @Builder.Default
    private Integer totalMatchesCreated = 0;

    @Builder.Default
    private Integer totalMatchesAccepted = 0;

    @Builder.Default
    private Double successRate = 0.0;
}
