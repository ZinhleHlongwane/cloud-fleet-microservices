package com.zinhle.cloudfleet.drone.api;

import com.zinhle.cloudfleet.drone.domain.DroneStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record DroneRequest(
        @NotBlank @Size(max = 40) String droneId,
        @NotBlank @Size(max = 80) String serialNumber,
        @NotBlank @Size(max = 120) String model,
        @NotNull DroneStatus status,
        @Min(0) @Max(100) int batteryLevel,
        @NotNull @DecimalMin(value = "0.1") BigDecimal maxPayloadKg,
        @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") Double latitude,
        @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") Double longitude
) {}
