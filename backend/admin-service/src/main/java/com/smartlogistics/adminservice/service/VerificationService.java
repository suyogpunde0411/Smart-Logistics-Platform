package com.smartlogistics.adminservice.service;

import com.smartlogistics.adminservice.dto.VerificationRequestDto;
import com.smartlogistics.adminservice.entity.VerificationRequest;
import com.smartlogistics.adminservice.exception.VerificationNotFoundException;
import com.smartlogistics.adminservice.mapper.AdminMapper;
import com.smartlogistics.adminservice.repository.VerificationRequestRepository;
import com.smartlogistics.adminservice.service.strategy.VerificationStrategy;
import com.smartlogistics.adminservice.service.strategy.VerificationStrategyRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class VerificationService {

    private final VerificationRequestRepository repository;
    private final VerificationStrategyRegistry strategyRegistry;
    private final AdminMapper mapper;

    public VerificationService(VerificationRequestRepository repository,
                               VerificationStrategyRegistry strategyRegistry,
                               AdminMapper mapper) {
        this.repository = repository;
        this.strategyRegistry = strategyRegistry;
        this.mapper = mapper;
    }

    public VerificationRequestDto submitRequest(VerificationRequestDto dto) {
        VerificationRequest request = mapper.toEntity(dto);
        request.setStatus("PENDING");
        return mapper.toDto(repository.save(request));
    }

    public List<VerificationRequestDto> getAllPendingRequests() {
        return repository.findByStatusAndIsDeletedFalse("PENDING").stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public VerificationRequestDto approveRequest(UUID id, UUID adminId, String comments) {
        VerificationRequest request = repository.findById(id)
                .orElseThrow(() -> new VerificationNotFoundException("Verification Request not found: " + id));

        VerificationStrategy strategy = strategyRegistry.getStrategy(request.getEntityType());
        strategy.process(request, "APPROVED", comments, adminId);

        request.setStatus("APPROVED");
        request.setComments(comments);
        request.setReviewedBy(adminId);
        request.setReviewedAt(java.time.LocalDateTime.now());
        
        return mapper.toDto(repository.save(request));
    }

    public VerificationRequestDto rejectRequest(UUID id, UUID adminId, String comments) {
        VerificationRequest request = repository.findById(id)
                .orElseThrow(() -> new VerificationNotFoundException("Verification Request not found: " + id));
        
        VerificationStrategy strategy = strategyRegistry.getStrategy(request.getEntityType());
        strategy.process(request, "REJECTED", comments, adminId);

        request.setStatus("REJECTED");
        request.setComments(comments);
        request.setReviewedBy(adminId);
        request.setReviewedAt(java.time.LocalDateTime.now());
        
        return mapper.toDto(repository.save(request));
    }
}
