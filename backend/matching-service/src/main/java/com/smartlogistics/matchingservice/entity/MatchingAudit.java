package com.smartlogistics.matchingservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "matching_audits")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingAudit extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String action;

    @Column
    private UUID shipmentId;

    @Column
    private UUID truckId;

    @Column
    private UUID actorId;

    @Column(length = 2000)
    private String details;

    @Column(length = 100)
    private String correlationId;
}
