package com.smartlogistics.adminservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import java.util.UUID;

@Entity
@Table(name = "document_verifications")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentVerification extends BaseEntity {
    private UUID verificationRequestId;
    private String documentName;
    private String documentType;
    private String documentUrl;
    private String status; // PENDING, APPROVED, REJECTED
}
