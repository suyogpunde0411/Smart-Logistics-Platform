package com.smartlogistics.notificationservice.repository;

import com.smartlogistics.notificationservice.entity.SmsQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SmsQueueRepository extends JpaRepository<SmsQueue, UUID> {
    List<SmsQueue> findByStatusInAndIsDeletedFalse(List<String> statuses);
    List<SmsQueue> findByStatusInAndNextRetryTimeBeforeAndIsDeletedFalse(List<String> statuses, LocalDateTime time);
}
