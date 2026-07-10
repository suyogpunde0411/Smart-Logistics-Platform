package com.smartlogistics.shipmentservice.config;

import com.smartlogistics.shipmentservice.entity.ShipmentCategory;
import com.smartlogistics.shipmentservice.repository.ShipmentCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ShipmentCategoryInitializer {

    private final ShipmentCategoryRepository categoryRepository;

    @Bean
    CommandLineRunner seedShipmentCategories() {
        return args -> defaultCategories().stream()
                .filter(category -> !categoryRepository.existsByCodeAndIsDeletedFalse(category.getCode()))
                .forEach(categoryRepository::save);
    }

    private List<ShipmentCategory> defaultCategories() {
        return List.of(
                category("GENERAL_CARGO", "General Cargo", "Standard packaged goods", false, false, false),
                category("FRAGILE", "Fragile", "Breakable or impact-sensitive goods", true, false, false),
                category("PERISHABLE", "Perishable", "Time-sensitive goods that may need refrigeration", true, true, false),
                category("HAZARDOUS", "Hazardous", "Regulated hazardous material", true, false, true),
                category("LIQUID", "Liquid", "Liquid cargo requiring compatible transport", true, false, false),
                category("CONTAINER", "Container", "Containerized cargo", false, false, false),
                category("HEAVY_EQUIPMENT", "Heavy Equipment", "Oversized machinery and industrial equipment", true, false, false),
                category("FURNITURE", "Furniture", "Household or commercial furniture", false, false, false),
                category("CONSTRUCTION_MATERIAL", "Construction Material", "Building and construction supplies", false, false, false),
                category("AGRICULTURE_PRODUCTS", "Agriculture Products", "Farm produce and agricultural inputs", true, false, false)
        );
    }

    private ShipmentCategory category(String code, String displayName, String description,
                                      boolean specialHandling, boolean refrigeration, boolean hazardous) {
        return ShipmentCategory.builder()
                .code(code)
                .displayName(displayName)
                .description(description)
                .active(true)
                .requiresSpecialHandling(specialHandling)
                .requiresRefrigeration(refrigeration)
                .isHazardous(hazardous)
                .build();
    }
}
