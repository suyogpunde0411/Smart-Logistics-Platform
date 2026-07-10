package com.smartlogistics.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "user_documents")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentityDocument extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile user;

    @Column(nullable = false, length = 50)
    private String type; // LICENSE, AADHAAR, PAN, GST, COMPANY_REG

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false, length = 20)
    private String status; // PENDING, VERIFIED, REJECTED
}
