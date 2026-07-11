package com.smartlogistics.adminservice.service;

import com.smartlogistics.adminservice.dto.SupportTicketDto;
import com.smartlogistics.adminservice.entity.SupportTicket;
import com.smartlogistics.adminservice.exception.SupportTicketNotFoundException;
import com.smartlogistics.adminservice.mapper.AdminMapper;
import com.smartlogistics.adminservice.repository.SupportTicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupportTicketService {

    private final SupportTicketRepository repository;
    private final AdminMapper mapper;

    public SupportTicketService(SupportTicketRepository repository, AdminMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public SupportTicketDto createTicket(SupportTicketDto dto) {
        SupportTicket ticket = mapper.toEntity(dto);
        ticket.setStatus("OPEN");
        return mapper.toDto(repository.save(ticket));
    }

    public List<SupportTicketDto> getAllTickets() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public SupportTicketDto resolveTicket(UUID ticketId, UUID adminId, String resolution) {
        SupportTicket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new SupportTicketNotFoundException("Ticket not found: " + ticketId));
        ticket.setStatus("RESOLVED");
        ticket.setResolvedBy(adminId);
        ticket.setResolution(resolution);
        return mapper.toDto(repository.save(ticket));
    }
}
