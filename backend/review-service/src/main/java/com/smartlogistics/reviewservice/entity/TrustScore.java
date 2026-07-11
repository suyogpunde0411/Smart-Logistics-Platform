package com.smartlogistics.reviewservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "trust_scores")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrustScore extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false)
    @Builder.Default
    private Integer score = 100; // 0 - 100 score range

    @Column(nullable = false)
    @Builder.Default
    private Double averageRating = 5.0;

    @Column(nullable = false)
    @Builder.Default
    private Integer completedTrips = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer cancelledTrips = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer reportedReviewsCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer disputeCount = 0;
}
