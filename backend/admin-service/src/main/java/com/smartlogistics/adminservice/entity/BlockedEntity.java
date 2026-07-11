package com.smartlogistics.adminservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import java.util.UUID;

@Entity
@Table(name = "blocked_entities")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockedEntity extends BaseEntity {
    private UUID blockedEntityId;
    private String entityType; // USER, TRUCK, BUSINESS
    private String reason;
    private UUID blockedBy;
}
