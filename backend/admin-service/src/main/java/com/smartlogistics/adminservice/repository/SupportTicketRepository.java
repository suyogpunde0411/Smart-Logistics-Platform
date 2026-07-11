package com.smartlogistics.adminservice.repository;

import com.smartlogistics.adminservice.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, UUID> {
    Optional<SupportTicket> findByTicketIdAndIsDeletedFalse(UUID ticketId);
    List<SupportTicket> findByStatusAndIsDeletedFalse(String status);
    List<SupportTicket> findByUserIdAndIsDeletedFalse(UUID userId);
}
