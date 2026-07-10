package com.smartlogistics.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private UUID id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private UUID createdBy;

    private UUID updatedBy;

    @Version
    private Integer version = 0;

    @Column(nullable = false)
    private boolean isDeleted = false;
    
    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        UUID currentUserId = com.smartlogistics.userservice.util.UserContext.getUserId();
        if (currentUserId != null) {
            this.createdBy = currentUserId;
            this.updatedBy = currentUserId;
        }
    }

    @PreUpdate
    public void preUpdate() {
        UUID currentUserId = com.smartlogistics.userservice.util.UserContext.getUserId();
        if (currentUserId != null) {
            this.updatedBy = currentUserId;
        }
    }
}
