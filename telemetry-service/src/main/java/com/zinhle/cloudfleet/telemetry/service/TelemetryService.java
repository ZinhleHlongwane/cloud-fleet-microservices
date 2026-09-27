package com.zinhle.cloudfleet.telemetry.service;

import com.zinhle.cloudfleet.events.FleetEvent;
import com.zinhle.cloudfleet.telemetry.api.*;
import com.zinhle.cloudfleet.telemetry.domain.TelemetrySample;
import com.zinhle.cloudfleet.telemetry.messaging.TelemetryEventPublisher;
import com.zinhle.cloudfleet.telemetry.repository.TelemetryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TelemetryService {
    private final TelemetryRepository repository;
    private final TelemetryEventPublisher publisher;

    public TelemetryService(TelemetryRepository repository, TelemetryEventPublisher publisher) {
        this.repository = repository; this.publisher = publisher;
    }

    public TelemetryResponse record(TelemetryRequest request) {
        TelemetrySample saved = repository.save(new TelemetrySample(
                request.droneId().trim(), request.batteryLevel(), request.latitude(), request.longitude(),
                request.altitudeMeters(), request.speedKph(), request.recordedAt()));
        if (request.batteryLevel() <= 20) {
            publisher.publish(FleetEvent.of("BatteryLow", "telemetry-service", request.droneId(),
                    Map.of("droneId", request.droneId(), "batteryLevel", request.batteryLevel())));
        }
        if (request.latitude() == null || request.longitude() == null) {
            publisher.publish(FleetEvent.of("InvalidTelemetry", "telemetry-service", request.droneId(),
                    Map.of("droneId", request.droneId(), "reason", "Missing coordinates")));
        }
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TelemetryResponse latest(String droneId) {
        return repository.findFirstByDroneIdOrderByRecordedAtDesc(droneId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("No telemetry found for drone: " + droneId));
    }

    @Transactional(readOnly = true)
    public List<TelemetryResponse> history(String droneId, int limit) {
        return repository.findByDroneIdOrderByRecordedAtDesc(droneId, PageRequest.of(0, Math.min(limit, 200)))
                .stream().map(this::toResponse).toList();
    }

    private TelemetryResponse toResponse(TelemetrySample t) {
        return new TelemetryResponse(t.getId(), t.getDroneId(), t.getBatteryLevel(), t.getLatitude(), t.getLongitude(),
                t.getAltitudeMeters(), t.getSpeedKph(), t.getRecordedAt());
    }
}
