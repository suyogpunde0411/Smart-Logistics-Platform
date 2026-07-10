package com.smartlogistics.userservice.domain.repository;

import com.smartlogistics.userservice.domain.entity.UserProfile;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID>, JpaSpecificationExecutor<UserProfile> {
    Optional<UserProfile> findByEmail(String email);
    Optional<UserProfile> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    @Query("SELECT DISTINCT u FROM UserProfile u " +
           "LEFT JOIN u.driverProfile d " +
           "LEFT JOIN u.businessProfile b " +
           "LEFT JOIN u.fleetOwnerProfile f " +
           "LEFT JOIN u.addresses a " +
           "WHERE u.isDeleted = false AND (" +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.phone) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.status) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.city) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(a.state) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(b.companyName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(f.companyName) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<UserProfile> searchUsers(@Param("query") String query, Pageable pageable);
}
