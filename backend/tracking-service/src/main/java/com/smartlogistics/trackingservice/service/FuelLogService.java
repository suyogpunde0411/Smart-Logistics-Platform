package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.dto.FuelLogDto;
import java.util.List;
import java.util.UUID;

public interface FuelLogService {
    FuelLogDto.Response addFuelLog(UUID tripId, FuelLogDto.CreateRequest request);
    List<FuelLogDto.Response> getFuelLogs(UUID tripId);
}
