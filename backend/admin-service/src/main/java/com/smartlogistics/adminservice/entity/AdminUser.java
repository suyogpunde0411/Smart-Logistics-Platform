package com.smartlogistics.adminservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "admin_users")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUser extends BaseEntity {
    private String username;
    private String passwordHash;
    private String email;
    private String name;
    private String role; // ADMIN, SUPER_ADMIN
}
