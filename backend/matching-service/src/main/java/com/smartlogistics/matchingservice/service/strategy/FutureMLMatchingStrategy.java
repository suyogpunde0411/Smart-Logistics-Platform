package com.smartlogistics.matchingservice.service.strategy;

import com.smartlogistics.matchingservice.client.ShipmentServiceClient.DetailedShipmentResponse;
import com.smartlogistics.matchingservice.dto.MatchResultDto;
import com.smartlogistics.matchingservice.entity.MatchRequest;
import com.smartlogistics.shared.dto.TruckDTO;

public class FutureMLMatchingStrategy implements MatchingStrategy {

    @Override
    public MatchResultDto.ScoreDetails calculateScore(
            MatchRequest request,
            DetailedShipmentResponse shipment,
            TruckDTO.Response truck) {
        throw new UnsupportedOperationException("ML Matching Strategy is not implemented yet. Clean Architecture allows easy integration of this strategy in the future.");
    }
}
