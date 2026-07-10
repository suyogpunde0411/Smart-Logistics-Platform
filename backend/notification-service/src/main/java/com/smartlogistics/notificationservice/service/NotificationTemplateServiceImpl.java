package com.smartlogistics.notificationservice.service;

import com.smartlogistics.notificationservice.dto.TemplateDto;
import com.smartlogistics.notificationservice.entity.NotificationTemplate;
import com.smartlogistics.notificationservice.exception.TemplateNotFoundException;
import com.smartlogistics.notificationservice.mapper.NotificationMapper;
import com.smartlogistics.notificationservice.repository.NotificationTemplateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private final NotificationTemplateRepository templateRepository;
    private final NotificationMapper mapper;

    @PostConstruct
    @Transactional
    public void initDefaultTemplates() {
        log.info("Initializing default notification templates in database...");
        seedTemplate("Welcome Email", "Welcome to Smart Logistics!", 
                "Dear ${firstName}, welcome to the Smart Logistics Optimization Platform! We are thrilled to have you.", "EMAIL", "SYSTEM");
        seedTemplate("Email Verification", "Verify your Email", 
                "Hi ${firstName}, please verify your email address using this verification code: ${code}", "EMAIL", "SYSTEM");
        seedTemplate("Password Reset", "Password Reset Link", 
                "Dear ${firstName}, reset your password by clicking here: ${link}", "EMAIL", "SYSTEM");
        seedTemplate("Truck Registered", "Truck Registered Successfully", 
                "Truck ${licensePlate} with capacity ${capacity} has been successfully registered to your fleet.", "EMAIL", "SYSTEM");
        seedTemplate("Shipment Created", "New Shipment Created", 
                "A new shipment with ID ${shipmentId} from ${origin} to ${destination} has been registered.", "EMAIL", "SHIPMENT_UPDATE");
        seedTemplate("Shipment Matched", "Shipment Matched", 
                "Your shipment ${shipmentId} has been successfully matched with truck ${truckId}!", "EMAIL", "SHIPMENT_UPDATE");
        seedTemplate("Bid Received", "New Bid Received", 
                "A new bid of ${price} has been placed on your shipment ${shipmentId}.", "EMAIL", "MARKETING");
        seedTemplate("Bid Accepted", "Bid Accepted!", 
                "Congratulations! Your bid of ${price} has been accepted for shipment ${shipmentId}.", "EMAIL", "MARKETING");
        seedTemplate("Trip Started", "Trip Started - En Route", 
                "Trip ${tripId} has officially started. Active GPS tracking is now live.", "PUSH", "TRIP_UPDATE");
        seedTemplate("Trip Completed", "Trip Completed Successfully", 
                "Trip ${tripId} has been completed successfully at destination address.", "PUSH", "TRIP_UPDATE");
        seedTemplate("Trip Cancelled", "Trip Cancelled Warning", 
                "Trip ${tripId} has been cancelled. Reason: ${reason}", "PUSH", "TRIP_UPDATE");
        seedTemplate("Document Expiring", "Compliance Document Expiring", 
                "Warning: Compliance document ${docName} is expiring on ${expiryDate}. Please renew it immediately.", "EMAIL", "SYSTEM");
        seedTemplate("Insurance Expiring", "Truck Insurance Expiring", 
                "Insurance policy for truck ID ${truckId} is expiring on ${expiryDate}.", "EMAIL", "SYSTEM");
        seedTemplate("Maintenance Reminder", "Truck Maintenance Scheduled", 
                "Truck ${truckId} is scheduled for dynamic maintenance audit on ${date}.", "EMAIL", "SYSTEM");
        seedTemplate("Review Request", "Leave feedback for Trip", 
                "How was your trip ${tripId}? Please leave feedback on matching driver and service.", "EMAIL", "MARKETING");
        log.info("Default templates initialization completed.");
    }

    private void seedTemplate(String name, String titleTemplate, String bodyTemplate, String channel, String type) {
        Optional<NotificationTemplate> existing = templateRepository.findByNameAndIsDeletedFalse(name);
        if (existing.isEmpty()) {
            NotificationTemplate template = NotificationTemplate.builder()
                    .name(name)
                    .titleTemplate(titleTemplate)
                    .bodyTemplate(bodyTemplate)
                    .channel(channel)
                    .type(type)
                    .build();
            templateRepository.save(template);
            log.info("Seeded template: {}", name);
        }
    }

    @Override
    @Transactional
    public TemplateDto.Response createTemplate(TemplateDto.CreateRequest request) {
        templateRepository.findByNameAndIsDeletedFalse(request.name()).ifPresent(t -> {
            throw new IllegalArgumentException("Template already exists with name: " + request.name());
        });
        NotificationTemplate template = mapper.toEntity(request);
        NotificationTemplate saved = templateRepository.save(template);
        return mapper.toResponse(saved);
    }

    @Override
    public TemplateDto.Response getTemplateById(UUID id) {
        NotificationTemplate template = templateRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TemplateNotFoundException("Template not found with ID: " + id));
        return mapper.toResponse(template);
    }

    @Override
    public TemplateDto.Response getTemplateByName(String name) {
        NotificationTemplate template = templateRepository.findByNameAndIsDeletedFalse(name)
                .orElseThrow(() -> new TemplateNotFoundException("Template not found with name: " + name));
        return mapper.toResponse(template);
    }

    @Override
    public List<TemplateDto.Response> getAllTemplates() {
        return templateRepository.findAll().stream()
                .filter(t -> !t.isDeleted())
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTemplate(UUID id) {
        NotificationTemplate template = templateRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new TemplateNotFoundException("Template not found with ID: " + id));
        template.setDeleted(true);
        templateRepository.save(template);
    }
}
