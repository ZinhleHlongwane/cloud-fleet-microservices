package com.zinhle.cloudfleet.mission.api;

import com.zinhle.cloudfleet.mission.domain.*;

import java.math.BigDecimal;
import java.time.Instant;

public record MissionResponse(
        Long id, String droneId, String origin, String destination, BigDecimal payloadKg,
        MissionPriority priority, MissionStatus status, Instant createdAt, Instant startedAt, Instant completedAt
) {}
