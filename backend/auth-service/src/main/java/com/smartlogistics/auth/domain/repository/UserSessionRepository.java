package com.smartlogistics.auth.domain.repository;

import com.smartlogistics.auth.domain.entity.UserSession;
import com.smartlogistics.auth.domain.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    List<UserSession> findByUserAndActiveTrue(UserCredential user);
}
