package com.smartlogistics.reviewservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "rating_statistics")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingStatistics extends BaseEntity {

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false)
    @Builder.Default
    private Double averageRating = 5.0;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalReviews = 0;

    @Builder.Default
    private Double avgCommunication = 0.0;
    
    @Builder.Default
    private Double avgPunctuality = 0.0;
    
    @Builder.Default
    private Double avgProfessionalism = 0.0;
    
    @Builder.Default
    private Double avgVehicleCondition = 0.0;
    
    @Builder.Default
    private Double avgCargoSafety = 0.0;
    
    @Builder.Default
    private Double avgPaymentExperience = 0.0;
    
    @Builder.Default
    private Double avgOverallExperience = 0.0;
}
