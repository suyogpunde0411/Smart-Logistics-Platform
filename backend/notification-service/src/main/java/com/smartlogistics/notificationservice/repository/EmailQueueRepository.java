package com.smartlogistics.notificationservice.repository;

import com.smartlogistics.notificationservice.entity.EmailQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EmailQueueRepository extends JpaRepository<EmailQueue, UUID> {
    List<EmailQueue> findByStatusInAndIsDeletedFalse(List<String> statuses);
    List<EmailQueue> findByStatusInAndNextRetryTimeBeforeAndIsDeletedFalse(List<String> statuses, LocalDateTime time);
}
