package com.smartlogistics.matchingservice.repository;

import com.smartlogistics.matchingservice.entity.MatchRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRuleRepository extends JpaRepository<MatchRule, UUID> {
    Optional<MatchRule> findByCodeAndIsDeletedFalse(String code);
    List<MatchRule> findByActiveTrueAndIsDeletedFalse();
    boolean existsByCodeAndIsDeletedFalse(String code);
}
