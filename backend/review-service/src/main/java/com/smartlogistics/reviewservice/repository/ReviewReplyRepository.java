package com.smartlogistics.reviewservice.repository;

import com.smartlogistics.reviewservice.entity.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewReplyRepository extends JpaRepository<ReviewReply, UUID> {
    Optional<ReviewReply> findByIdAndIsDeletedFalse(UUID id);
}
