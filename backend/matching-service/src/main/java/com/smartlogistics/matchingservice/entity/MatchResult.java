package com.smartlogistics.matchingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "match_results",
        uniqueConstraints = @UniqueConstraint(name = "uk_match_result_request_truck", columnNames = {"match_request_id", "truck_id"}))
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_request_id", nullable = false)
    private MatchRequest matchRequest;

    @Column(nullable = false)
    private UUID shipmentId;

    @Column(nullable = false)
    private UUID truckId;

    @Column
    private UUID driverId;

    @Column(nullable = false)
    private Double overallScore;

    @Column
    private Double distanceScore;

    @Column
    private Double capacityScore;

    @Column
    private Double availabilityScore;

    @Column
    private Double compatibilityScore;

    @Column
    private Double estimatedEtaMinutes;

    @Column
    private Double estimatedCost;

    @Column(length = 2000)
    private String reasoning;

    @Column(nullable = false, length = 50)
    private String status;

    @Column
    private LocalDateTime expiresAt;
}
