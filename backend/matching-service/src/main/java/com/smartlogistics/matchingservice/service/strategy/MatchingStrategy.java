package com.smartlogistics.matchingservice.service.strategy;

import com.smartlogistics.matchingservice.client.ShipmentServiceClient.DetailedShipmentResponse;
import com.smartlogistics.matchingservice.dto.MatchResultDto;
import com.smartlogistics.matchingservice.entity.MatchRequest;
import com.smartlogistics.shared.dto.TruckDTO;

public interface MatchingStrategy {
    MatchResultDto.ScoreDetails calculateScore(
            MatchRequest request,
            DetailedShipmentResponse shipment,
            TruckDTO.Response truck);
}
