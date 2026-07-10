package com.smartlogistics.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "business_profiles")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserProfile user;

    @Column(nullable = false, length = 150)
    private String companyName;

    @Column(length = 50)
    private String taxId; // GST Number

    @Column(length = 255)
    private String website;
}
