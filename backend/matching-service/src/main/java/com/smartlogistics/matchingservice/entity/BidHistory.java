package com.smartlogistics.matchingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bid_history")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id", nullable = false)
    private Bid bid;

    @Column(length = 50)
    private String oldStatus;

    @Column(nullable = false, length = 50)
    private String newStatus;

    @Column
    private UUID changedBy;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @Column(length = 1000)
    private String remarks;
}
