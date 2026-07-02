package com.smartlogistics.auth.domain.repository;

import com.smartlogistics.auth.domain.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID> {
    Optional<Otp> findByIdentifierAndCode(String identifier, String code);
    void deleteByIdentifier(String identifier);
}
