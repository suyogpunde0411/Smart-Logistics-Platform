package com.smartlogistics.matchingservice.service.strategy;

import com.smartlogistics.common.client.ReviewFeignClient;
import com.smartlogistics.common.client.TruckFeignClient;
import com.smartlogistics.matchingservice.client.ShipmentServiceClient.DetailedShipmentResponse;
import com.smartlogistics.matchingservice.constants.MatchingConstants;
import com.smartlogistics.matchingservice.dto.MatchResultDto;
import com.smartlogistics.matchingservice.entity.MatchRequest;
import com.smartlogistics.matchingservice.entity.MatchRule;
import com.smartlogistics.matchingservice.repository.MatchRuleRepository;
import com.smartlogistics.shared.dto.TruckDTO;
import com.smartlogistics.shared.util.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleBasedMatchingStrategy implements MatchingStrategy {

    private final MatchRuleRepository ruleRepository;
    private final ReviewFeignClient reviewFeignClient;
    private final TruckFeignClient truckFeignClient;

    @Override
    public MatchResultDto.ScoreDetails calculateScore(
            MatchRequest request,
            DetailedShipmentResponse shipment,
            TruckDTO.Response truck) {

        // 1. Collect all active rules
        List<MatchRule> activeRules = ruleRepository.findByActiveTrueAndIsDeletedFalse();
        Map<String, Double> weights = new HashMap<>();
        for (MatchRule rule : activeRules) {
            weights.put(rule.getCode(), rule.getWeight());
        }

        // Coordinates
        double truckLat = truck.location() != null ? truck.location().latitude() : 0.0;
        double truckLng = truck.location() != null ? truck.location().longitude() : 0.0;
        double originLat = shipment.originLatitude() != null ? shipment.originLatitude() : 0.0;
        double originLng = shipment.originLongitude() != null ? shipment.originLongitude() : 0.0;
        double destLat = shipment.destinationLatitude() != null ? shipment.destinationLatitude() : 0.0;
        double destLng = shipment.destinationLongitude() != null ? shipment.destinationLongitude() : 0.0;

        // Pickup Distance calculation
        double pickupDistance = DistanceCalculator.calculateDistance(truckLat, truckLng, originLat, originLng);
        double maxDist = request.getMaxDistanceKm() != null ? request.getMaxDistanceKm() : 100.0;

        // 2. Compute Individual Scores
        // 2a. Pickup Distance Score
        double distanceScore = 0.0;
        if (pickupDistance <= maxDist) {
            distanceScore = 100.0 * (1.0 - (pickupDistance / maxDist));
        }

        // 2b. Destination Similarity Score
        double shipmentDist = DistanceCalculator.calculateDistance(originLat, originLng, destLat, destLng);
        double distToDest = DistanceCalculator.calculateDistance(truckLat, truckLng, destLat, destLng);
        double similarityScore = 100.0;
        if (shipmentDist > 0.0) {
            similarityScore = 100.0 * (1.0 - Math.min(1.0, distToDest / (shipmentDist * 1.5)));
        }
        similarityScore = Math.max(0.0, similarityScore);

        // 2c. Capacity Score
        double capacityScore = 0.0;
        boolean weightOk = truck.capacity() != null && truck.capacity().maxWeight() >= shipment.totalWeight();
        boolean volumeOk = true;
        if (shipment.totalVolume() != null && truck.capacity() != null && truck.capacity().maxVolume() != null) {
            volumeOk = truck.capacity().maxVolume() >= shipment.totalVolume();
        }
        if (weightOk && volumeOk) {
            double weightRatio = truck.capacity().maxWeight() > 0 ? shipment.totalWeight() / truck.capacity().maxWeight() : 1.0;
            double volumeRatio = (shipment.totalVolume() != null && truck.capacity().maxVolume() > 0)
                    ? shipment.totalVolume() / truck.capacity().maxVolume()
                    : 1.0;
            capacityScore = 50.0 + 50.0 * ((weightRatio + volumeRatio) / 2.0);
        }

        // 2d. Cargo Compatibility Score
        double cargoScore = 100.0;
        if (shipment.requiredTruckType() != null && !shipment.requiredTruckType().isBlank()) {
            String truckType = "CONTAINER";
            try {
                var internalTruck = truckFeignClient.getTruck(truck.id());
                if (internalTruck != null && internalTruck.type() != null) {
                    truckType = internalTruck.type();
                }
            } catch (Exception e) {
                log.warn("Failed to fetch internal truck type for matching truck: {}, fallback to CONTAINER", truck.id());
            }

            if (truckType.equalsIgnoreCase(shipment.requiredTruckType())) {
                cargoScore = 100.0;
            } else if (shipment.requiredTruckType().equalsIgnoreCase("ANY")) {
                cargoScore = 80.0;
            } else {
                cargoScore = 0.0;
            }
        }

        // 2e. Availability Score
        double availabilityScore = 0.0;
        if ("AVAILABLE".equalsIgnoreCase(truck.status()) || "ACTIVE".equalsIgnoreCase(truck.status())) {
            availabilityScore = 100.0;
        }

        // 2f. Vehicle Type Score
        double vehicleTypeScore = cargoScore; // Corresponds directly in our context

        // 2g. Driver Rating Score
        double ratingScore = 80.0; // Fallback default (corresponds to 4.0 stars)
        try {
            Double avgRating = reviewFeignClient.getAverageRating(truck.ownerId());
            if (avgRating != null && avgRating > 0) {
                ratingScore = avgRating * 20.0;
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve driver rating for: {}, fallback to default rating score", truck.ownerId());
        }

        // 2h. Business Preference Score
        double businessPrefScore = 80.0; // Default fallback

        // 3. Overall Score Summation with weight rules
        double totalWeightedScore = 0.0;
        double totalWeight = 0.0;

        // Map scores to their codes
        Map<String, Double> scores = new HashMap<>();
        scores.put(MatchingConstants.RULE_PICKUP_DISTANCE, distanceScore);
        scores.put(MatchingConstants.RULE_DESTINATION_SIMILARITY, similarityScore);
        scores.put(MatchingConstants.RULE_TRUCK_CAPACITY, capacityScore);
        scores.put(MatchingConstants.RULE_CARGO_COMPATIBILITY, cargoScore);
        scores.put(MatchingConstants.RULE_AVAILABILITY, availabilityScore);
        scores.put(MatchingConstants.RULE_VEHICLE_TYPE, vehicleTypeScore);
        scores.put(MatchingConstants.RULE_DRIVER_RATING, ratingScore);
        scores.put(MatchingConstants.RULE_BUSINESS_PREFERENCE, businessPrefScore);

        for (Map.Entry<String, Double> entry : weights.entrySet()) {
            String ruleCode = entry.getKey();
            Double ruleWeight = entry.getValue();
            Double score = scores.getOrDefault(ruleCode, 0.0);

            totalWeightedScore += (score * ruleWeight);
            totalWeight += ruleWeight;
        }

        double overallScore = totalWeight > 0.0 ? (totalWeightedScore / totalWeight) : 0.0;

        // Hard constraints: if any hard constraint fails, overall score is zero.
        if (pickupDistance > maxDist || !weightOk || !volumeOk || cargoScore == 0.0 || availabilityScore == 0.0) {
            overallScore = 0.0;
        }

        // Estimated ETA & Cost
        double speed = (truck.location() != null && truck.location().speed() != null && truck.location().speed() > 0)
                ? truck.location().speed()
                : 50.0; // default 50 km/h
        double eta = (pickupDistance / speed) * 60.0 + 10.0; // in minutes

        double ratePerKm = 15.0; // INR per km
        double estimatedCost = shipmentDist * ratePerKm * (1.0 + shipment.totalWeight() / 10000.0);

        StringBuilder reasoning = new StringBuilder();
        reasoning.append("Calculated matching score using rule-based metrics. ")
                .append(String.format("Distance: %.1f km (score: %.1f). ", pickupDistance, distanceScore))
                .append(String.format("Capacity match: %s (score: %.1f). ", (weightOk && volumeOk) ? "PASS" : "FAIL", capacityScore))
                .append(String.format("Cargo compatibility: %.1f. ", cargoScore))
                .append(String.format("Driver rating score: %.1f.", ratingScore));

        return new MatchResultDto.ScoreDetails(
                overallScore,
                distanceScore,
                capacityScore,
                availabilityScore,
                cargoScore,
                eta,
                estimatedCost,
                reasoning.toString()
        );
    }
}
