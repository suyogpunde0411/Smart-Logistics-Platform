package com.smartlogistics.adminservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import java.util.UUID;

@Entity
@Table(name = "moderation_records")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModerationRecord extends BaseEntity {
    private UUID reportedEntityId;
    private String entityType; // REVIEW, COMMENT
    private String actionTaken; // REMOVED, APPROVED
    private String reason;
    private UUID moderatedBy;
}
