package com.smartlogistics.adminservice.service;

import com.smartlogistics.adminservice.dto.AdminDashboardResponseDto;
import com.smartlogistics.adminservice.repository.VerificationRequestRepository;
import com.smartlogistics.adminservice.repository.SupportTicketRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardService {

    private final VerificationRequestRepository verificationRequestRepository;
    private final SupportTicketRepository supportTicketRepository;

    public AdminDashboardService(VerificationRequestRepository verificationRequestRepository,
                                 SupportTicketRepository supportTicketRepository) {
        this.verificationRequestRepository = verificationRequestRepository;
        this.supportTicketRepository = supportTicketRepository;
    }

    public AdminDashboardResponseDto getDashboardStats() {
        long pendingVerifications = verificationRequestRepository.findByStatusAndIsDeletedFalse("PENDING").size();
        long openTickets = supportTicketRepository.findAll().stream()
                .filter(t -> "OPEN".equals(t.getStatus()))
                .count();

        return new AdminDashboardResponseDto(
                1000L, // totalUsers
                500L,  // totalTrucks
                200L,  // totalShipments
                150L,  // activeTrips
                pendingVerifications, // pendingDriverVerifications (mocked grouped)
                pendingVerifications, // pendingBusinessVerifications (mocked grouped)
                pendingVerifications, // pendingTruckVerifications (mocked grouped)
                openTickets
        );
    }
}
