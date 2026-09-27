package com.zinhle.cloudfleet.maintenance.api;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record MaintenanceRequest(
        @NotBlank String droneId,
        @NotBlank @Size(max = 80) String maintenanceType,
        @NotNull LocalDate scheduledDate,
        @Size(max = 1000) String notes
) {}
