package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.dto.MatchRuleDto;
import java.util.List;

public interface RuleService {
    List<MatchRuleDto.Response> getAllRules();
    MatchRuleDto.Response updateRule(String code, MatchRuleDto.UpdateRequest request);
}
