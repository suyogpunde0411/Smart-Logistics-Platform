package com.smartlogistics.analyticsservice.repository;

import com.smartlogistics.analyticsservice.entity.KpiSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface KpiSnapshotRepository extends JpaRepository<KpiSnapshot, UUID> {
    List<KpiSnapshot> findByKpiKeyAndIsDeletedFalseOrderBySnapshotTimeDesc(String kpiKey);
}
