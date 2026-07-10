package com.smartlogistics.shared.dto;

import java.time.LocalDateTime;

public record LocationDTO(
        Double latitude,
        Double longitude,
        Double speed,
        Double heading,
        Double accuracy,
        LocalDateTime timestamp
) {}
