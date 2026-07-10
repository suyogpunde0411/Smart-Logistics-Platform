package com.smartlogistics.matchingservice.service;

import com.smartlogistics.matchingservice.dto.BidDto;
import java.util.UUID;

public interface BidService {
    BidDto.Response placeBid(BidDto.CreateRequest request);
    BidDto.Response acceptBid(UUID bidId);
    BidDto.Response rejectBid(UUID bidId, BidDto.RejectRequest request);
    void completeMatchWorkflow(UUID shipmentId, UUID truckId);
}
