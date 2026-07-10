package com.smartlogistics.truckservice.controller;

import com.smartlogistics.common.client.TruckFeignClient.InternalTruckResponse;
import com.smartlogistics.shared.dto.TruckDTO;
import com.smartlogistics.truckservice.service.TruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/internal/trucks")
@RequiredArgsConstructor
public class TruckInternalController {

    private final TruckService truckService;

    @GetMapping("/{id}")
    public ResponseEntity<InternalTruckResponse> getTruck(@PathVariable("id") UUID id) {
        TruckDTO.Response response = truckService.getTruck(id);
        
        InternalTruckResponse internalResponse = new InternalTruckResponse(
                response.id(),
                response.ownerId(),
                response.registrationNumber(),
                "CONTAINER",
                response.capacity() != null ? response.capacity().maxWeight() : 0.0,
                response.status()
        );
        return ResponseEntity.ok(internalResponse);
    }
}
