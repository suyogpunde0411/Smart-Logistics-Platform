package com.smartlogistics.adminservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import java.util.UUID;

@Entity
@Table(name = "support_tickets")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicket extends BaseEntity {
    private UUID ticketId;
    private UUID userId;
    private String title;
    private String description;
    private String category;
    private String status; // OPEN, IN_PROGRESS, RESOLVED
    private String priority;
    private String comments;
    private UUID assignedTo;
    private UUID resolvedBy;
    private String resolution;
}
