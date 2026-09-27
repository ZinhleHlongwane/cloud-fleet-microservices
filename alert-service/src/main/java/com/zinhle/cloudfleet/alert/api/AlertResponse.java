package com.zinhle.cloudfleet.alert.api;

import com.zinhle.cloudfleet.alert.domain.AlertSeverity;
import java.time.Instant;

public record AlertResponse(
        Long id, String eventType, String aggregateId, AlertSeverity severity,
        String message, boolean acknowledged, Instant createdAt, Instant acknowledgedAt
) {}
