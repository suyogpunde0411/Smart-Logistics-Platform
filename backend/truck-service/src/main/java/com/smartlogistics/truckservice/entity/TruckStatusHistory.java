package com.smartlogistics.truckservice.entity;

import jakarta.persistence.*;
import lombok.*;
import com.smartlogistics.shared.auditing.BaseEntity;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "truck_status_history")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckStatusHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", nullable = false)
    private Truck truck;

    @Column(nullable = false, length = 50)
    private String oldStatus;

    @Column(nullable = false, length = 50)
    private String newStatus;

    private UUID changedBy;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    @Column(length = 500)
    private String remarks;
}
