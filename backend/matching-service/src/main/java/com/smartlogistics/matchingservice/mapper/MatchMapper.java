package com.smartlogistics.matchingservice.mapper;

import com.smartlogistics.matchingservice.dto.*;
import com.smartlogistics.matchingservice.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchMapper {

    MatchRequestDto.Response toDto(MatchRequest request);

    @Mapping(target = "matchRequestId", source = "matchRequest.id")
    MatchResultDto.Response toDto(MatchResult result);

    @Mapping(target = "matchResultId", source = "matchResult.id")
    BidDto.Response toDto(Bid bid);

    MatchRuleDto.Response toDto(MatchRule rule);
}
