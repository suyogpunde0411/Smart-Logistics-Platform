package com.smartlogistics.analyticsservice.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DashboardBuilderFactory {

    private final AdminDashboardBuilder adminBuilder;
    private final DriverDashboardBuilder driverBuilder;
    private final BusinessDashboardBuilder businessBuilder;
    private final FleetDashboardBuilder fleetBuilder;

    public DashboardBuilder getBuilder(String role) {
        if (role == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }
        
        return switch (role.toUpperCase()) {
            case "ADMIN" -> adminBuilder;
            case "DRIVER" -> driverBuilder;
            case "BUSINESS_OWNER" -> businessBuilder;
            case "FLEET_OWNER" -> fleetBuilder;
            default -> throw new IllegalArgumentException("Unsupported role for dashboard generation: " + role);
        };
    }
}
