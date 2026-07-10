package com.smartlogistics.matchingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "accepted_matches")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcceptedMatch extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_result_id", nullable = false, unique = true)
    private MatchResult matchResult;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id", unique = true)
    private Bid bid;

    @Column(nullable = false)
    private LocalDateTime acceptedAt;
}
