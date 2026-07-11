package com.smartlogistics.adminservice.dto;

public record AdminDashboardResponseDto(
        long totalUsers,
        long totalTrucks,
        long totalShipments,
        long activeTrips,
        long pendingDriverVerifications,
        long pendingBusinessVerifications,
        long pendingTruckVerifications,
        long openSupportTickets
) {}
