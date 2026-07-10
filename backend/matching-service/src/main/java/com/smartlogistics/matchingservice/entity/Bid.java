package com.smartlogistics.matchingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bids",
        uniqueConstraints = @UniqueConstraint(name = "uk_bid_match_driver", columnNames = {"match_result_id", "driver_id"}))
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_result_id", nullable = false)
    private MatchResult matchResult;

    @Column(nullable = false)
    private UUID shipmentId;

    @Column(nullable = false)
    private UUID truckId;

    @Column(nullable = false)
    private UUID driverId;

    @Column
    private UUID businessId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, length = 10)
    private String currency;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(length = 1000)
    private String message;

    @Column
    private LocalDateTime expiresAt;
}
