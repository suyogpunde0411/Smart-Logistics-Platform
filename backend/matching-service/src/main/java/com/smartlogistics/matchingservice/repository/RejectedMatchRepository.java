package com.smartlogistics.matchingservice.repository;

import com.smartlogistics.matchingservice.entity.RejectedMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RejectedMatchRepository extends JpaRepository<RejectedMatch, UUID> {
}
