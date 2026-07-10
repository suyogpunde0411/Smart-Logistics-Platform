package com.smartlogistics.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "preferences")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preference extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserProfile user;

    @Column(length = 10)
    @Builder.Default
    private String language = "en"; // en, hi, mr
    
    @Column(length = 50)
    @Builder.Default
    private String theme = "LIGHT"; // LIGHT, DARK
}
