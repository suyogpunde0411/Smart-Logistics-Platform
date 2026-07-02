package com.smartlogistics.auth.domain.repository;

import com.smartlogistics.auth.domain.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, UUID> {
    long countByEmailAndSuccessFalseAndAttemptTimeAfter(String email, LocalDateTime time);
}
