package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.constants.MatchingConstants;
import com.smartlogistics.matchingservice.entity.MatchRule;
import com.smartlogistics.matchingservice.repository.MatchRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchRuleSeeder implements CommandLineRunner {

    private final MatchRuleRepository ruleRepository;

    @Override
    public void run(String... args) {
        log.info("Checking if default matching rules are seeded...");
        seedRule(MatchingConstants.RULE_PICKUP_DISTANCE, 30.0, "Pickup distance between truck location and shipment origin");
        seedRule(MatchingConstants.RULE_DESTINATION_SIMILARITY, 10.0, "Direction/distance similarity to typical route or preference");
        seedRule(MatchingConstants.RULE_TRUCK_CAPACITY, 15.0, "Truck capacity comparison with shipment weight and volume");
        seedRule(MatchingConstants.RULE_CARGO_COMPATIBILITY, 10.0, "Cargo compatibility with truck equipment specifications");
        seedRule(MatchingConstants.RULE_AVAILABILITY, 10.0, "Truck current availability status");
        seedRule(MatchingConstants.RULE_VEHICLE_TYPE, 10.0, "Required truck type match");
        seedRule(MatchingConstants.RULE_DRIVER_RATING, 10.0, "Average driver rating from reviews");
        seedRule(MatchingConstants.RULE_BUSINESS_PREFERENCE, 5.0, "Business owner historical acceptance preferences");
    }

    private void seedRule(String code, double weight, String description) {
        if (!ruleRepository.existsByCodeAndIsDeletedFalse(code)) {
            MatchRule rule = MatchRule.builder()
                    .code(code)
                    .weight(weight)
                    .active(true)
                    .description(description)
                    .build();
            ruleRepository.save(rule);
            log.info("Seeded matching rule: {} with weight: {}", code, weight);
        }
    }
}
