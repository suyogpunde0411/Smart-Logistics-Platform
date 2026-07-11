package com.smartlogistics.adminservice.service.strategy;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VerificationStrategyRegistry {

    private final Map<String, VerificationStrategy> strategies = new HashMap<>();

    public VerificationStrategyRegistry(List<VerificationStrategy> strategyList) {
        for (VerificationStrategy strategy : strategyList) {
            strategies.put(strategy.getVerificationType().toUpperCase(), strategy);
        }
    }

    public VerificationStrategy getStrategy(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Verification type cannot be null");
        }
        VerificationStrategy strategy = strategies.get(type.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("No verification strategy found for type: " + type);
        }
        return strategy;
    }
}
