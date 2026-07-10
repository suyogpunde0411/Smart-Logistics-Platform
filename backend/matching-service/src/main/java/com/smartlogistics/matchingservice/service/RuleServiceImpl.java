package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.dto.MatchRuleDto;
import com.smartlogistics.matchingservice.entity.MatchRule;
import com.smartlogistics.matchingservice.mapper.MatchMapper;
import com.smartlogistics.matchingservice.repository.MatchRuleRepository;
import com.smartlogistics.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleServiceImpl implements RuleService {

    private final MatchRuleRepository ruleRepository;
    private final MatchMapper mapper;

    @Override
    public List<MatchRuleDto.Response> getAllRules() {
        return ruleRepository.findAll().stream()
                .filter(rule -> !rule.isDeleted())
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MatchRuleDto.Response updateRule(String code, MatchRuleDto.UpdateRequest request) {
        MatchRule rule = ruleRepository.findByCodeAndIsDeletedFalse(code)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found with code: " + code));

        rule.setWeight(request.weight());
        rule.setActive(request.active());
        MatchRule saved = ruleRepository.save(rule);
        log.info("Updated matching rule {} to weight {} and active status {}", code, request.weight(), request.active());
        return mapper.toDto(saved);
    }
}
