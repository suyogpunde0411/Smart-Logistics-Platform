package com.smartlogistics.adminservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verification_requests")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationRequest extends BaseEntity {
    private UUID entityId;
    private String entityType; // DRIVER, BUSINESS, FLEET, TRUCK
    private String status; // PENDING, APPROVED, REJECTED, REUPLOAD_REQUESTED
    private String comments;
    private UUID reviewedBy;
    private LocalDateTime reviewedAt;
}
