package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "review_analytics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewAnalytics extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID revieweeId;

    private String revieweeRole;

    @Builder.Default
    private Double averageRating = 5.0;

    @Builder.Default
    private Integer totalReviews = 0;
}
