package com.smartlogistics.notificationservice.repository;

import com.smartlogistics.notificationservice.entity.PushQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PushQueueRepository extends JpaRepository<PushQueue, UUID> {
    List<PushQueue> findByStatusInAndIsDeletedFalse(List<String> statuses);
    List<PushQueue> findByStatusInAndNextRetryTimeBeforeAndIsDeletedFalse(List<String> statuses, LocalDateTime time);
}
