package com.smartlogistics.matchingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "match_requests")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchRequest extends BaseEntity {

    @Column(nullable = false)
    private UUID shipmentId;

    @Column
    private UUID businessId;

    @Column(length = 50)
    private String status;

    @Column
    private Double pickupLatitude;

    @Column
    private Double pickupLongitude;

    @Column
    private Double destinationLatitude;

    @Column
    private Double destinationLongitude;

    @Column
    private Double radiusKm;

    @Column
    private Double maxDistanceKm;

    @Column
    private LocalDateTime expiresAt;
}
