package com.zinhle.cloudfleet.telemetry.api;

import java.time.Instant;

public record TelemetryResponse(
        Long id, String droneId, int batteryLevel, Double latitude, Double longitude,
        Double altitudeMeters, Double speedKph, Instant recordedAt
) {}
