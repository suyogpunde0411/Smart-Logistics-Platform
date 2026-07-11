package com.smartlogistics.adminservice.service;

import com.smartlogistics.adminservice.dto.AuditLogDto;
import com.smartlogistics.adminservice.mapper.AdminMapper;
import com.smartlogistics.adminservice.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuditService {

    private final AuditLogRepository repository;
    private final AdminMapper mapper;

    public AuditService(AuditLogRepository repository, AdminMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AuditLogDto> getAllAuditLogs() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
