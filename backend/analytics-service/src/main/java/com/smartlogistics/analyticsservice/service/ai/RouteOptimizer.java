package com.smartlogistics.analyticsservice.service.ai;

import java.util.List;

public interface RouteOptimizer {
    /**
     * Compute optimized list of waypoints to minimize fuel usage and distance.
     */
    List<String> optimizeRoute(String origin, String destination, List<String> waypoints);
}
