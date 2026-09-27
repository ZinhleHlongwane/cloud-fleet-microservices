package com.zinhle.cloudfleet.events;

import java.time.Instant;
import java.util.Map;

public record FleetEvent(
        String eventType,
        String sourceService,
        String aggregateId,
        Instant occurredAt,
        Map<String, Object> data
) {
    public static FleetEvent of(String eventType, String sourceService, String aggregateId, Map<String, Object> data) {
        return new FleetEvent(eventType, sourceService, aggregateId, Instant.now(), data);
    }
}
