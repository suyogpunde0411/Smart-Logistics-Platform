package com.smartlogistics.adminservice.repository;

import com.smartlogistics.adminservice.entity.AdminAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AdminActionRepository extends JpaRepository<AdminAction, UUID> {
}
