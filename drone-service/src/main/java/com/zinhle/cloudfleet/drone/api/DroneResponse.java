package com.zinhle.cloudfleet.drone.api;

import com.zinhle.cloudfleet.drone.domain.DroneStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record DroneResponse(
        String droneId,
        String serialNumber,
        String model,
        DroneStatus status,
        int batteryLevel,
        BigDecimal maxPayloadKg,
        Double latitude,
        Double longitude,
        Instant createdAt,
        Instant updatedAt
) {}
