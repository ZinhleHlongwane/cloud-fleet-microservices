package com.zinhle.cloudfleet.telemetry.api;

import com.zinhle.cloudfleet.telemetry.service.TelemetryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryController {

    private final TelemetryService service;

    public TelemetryController(TelemetryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TelemetryResponse> record(
            @Valid @RequestBody TelemetryRequest request) {

        TelemetryResponse created = service.record(request);

        return ResponseEntity
                .created(URI.create("/api/telemetry/" + created.id()))
                .body(created);
    }

    @GetMapping("/drone/{droneId}/latest")
    public TelemetryResponse latest(
            @PathVariable(name = "droneId") String droneId) {

        return service.latest(droneId);
    }

    @GetMapping("/drone/{droneId}")
    public List<TelemetryResponse> history(
            @PathVariable(name = "droneId") String droneId,
            @RequestParam(name = "limit", defaultValue = "50")
            @Min(1) @Max(200) int limit) {

        return service.history(droneId, limit);
    }
}