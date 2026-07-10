package com.smartlogistics.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "user_settings")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettings extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserProfile user;

    @Builder.Default
    @Column(nullable = false)
    private boolean emailNotificationsEnabled = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean smsNotificationsEnabled = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean pushNotificationsEnabled = true;
    
    @Builder.Default
    @Column(nullable = false)
    private boolean twoFactorEnabled = false;
}
