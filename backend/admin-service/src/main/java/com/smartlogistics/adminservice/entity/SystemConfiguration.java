package com.smartlogistics.adminservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "system_configurations")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfiguration extends BaseEntity {
    private String configKey;
    private String configValue;
    private String description;
    private String module; // REGISTRATION, NOTIFICATION, MATCHING, ANALYTICS, AI_MODULE
}
