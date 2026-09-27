package com.zinhle.cloudfleet.telemetry.api;

import jakarta.validation.constraints.*;
import java.time.Instant;

public record TelemetryRequest(
        @NotBlank String droneId,
        @Min(0) @Max(100) int batteryLevel,
        @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
        @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
        @PositiveOrZero Double altitudeMeters,
        @PositiveOrZero Double speedKph,
        Instant recordedAt
) {}
