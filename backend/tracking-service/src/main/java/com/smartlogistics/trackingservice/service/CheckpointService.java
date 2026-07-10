package com.smartlogistics.trackingservice.service;

import com.smartlogistics.trackingservice.dto.CheckpointDto;
import java.util.List;
import java.util.UUID;

public interface CheckpointService {
    CheckpointDto.Response addCheckpoint(UUID tripId, CheckpointDto.CreateRequest request);
    CheckpointDto.Response reachCheckpoint(UUID tripId, UUID checkpointId);
    CheckpointDto.Response departCheckpoint(UUID tripId, UUID checkpointId);
    List<CheckpointDto.Response> getCheckpoints(UUID tripId);
}
