package com.zinhle.cloudfleet.maintenance.api;

import java.time.*;

public record MaintenanceResponse(
        Long id, String droneId, String maintenanceType, LocalDate scheduledDate,
        LocalDate completedDate, String notes, boolean overdue, Instant createdAt
) {}
