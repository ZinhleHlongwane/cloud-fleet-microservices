package com.zinhle.cloudfleet.mission.api;

import com.zinhle.cloudfleet.mission.domain.MissionPriority;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record MissionRequest(
        @NotBlank @Size(max = 160) String origin,
        @NotBlank @Size(max = 160) String destination,
        @NotNull @DecimalMin("0.1") BigDecimal payloadKg,
        @NotNull MissionPriority priority
) {}
