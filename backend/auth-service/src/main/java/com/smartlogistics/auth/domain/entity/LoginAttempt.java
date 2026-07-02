package com.smartlogistics.auth.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "login_attempts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginAttempt {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 50)
    private String ipAddress;

    @Builder.Default
    @Column(nullable = false)
    private boolean success = false;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime attemptTime;
}
