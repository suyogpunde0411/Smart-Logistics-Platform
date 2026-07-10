package com.smartlogistics.matchingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "recommendation_logs")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationLog extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String recommendationType;

    @Column
    private UUID shipmentId;

    @Column
    private UUID truckId;

    @Column
    private UUID requestedBy;

    @Column
    private Integer resultCount;

    @Column(length = 1000)
    private String provider;
}
