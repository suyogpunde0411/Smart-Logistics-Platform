package com.smartlogistics.analyticsservice.entity;

import com.smartlogistics.shared.auditing.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "kpi_snapshots")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KpiSnapshot extends BaseEntity {

    private LocalDateTime snapshotTime;

    private String kpiKey;

    private Double kpiValue;
}
