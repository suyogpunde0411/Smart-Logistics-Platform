package com.smartlogistics.auth.domain.repository;

import com.smartlogistics.auth.domain.entity.RefreshToken;
import com.smartlogistics.auth.domain.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(UserCredential user);
}
