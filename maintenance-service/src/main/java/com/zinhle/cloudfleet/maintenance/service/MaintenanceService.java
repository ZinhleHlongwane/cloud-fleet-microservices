package com.zinhle.cloudfleet.maintenance.service;

import com.zinhle.cloudfleet.events.FleetEvent;
import com.zinhle.cloudfleet.maintenance.api.*;
import com.zinhle.cloudfleet.maintenance.domain.MaintenanceRecord;
import com.zinhle.cloudfleet.maintenance.messaging.MaintenanceEventPublisher;
import com.zinhle.cloudfleet.maintenance.repository.MaintenanceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class MaintenanceService {
    private final MaintenanceRepository repository;
    private final MaintenanceEventPublisher publisher;

    public MaintenanceService(MaintenanceRepository repository, MaintenanceEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public MaintenanceResponse schedule(MaintenanceRequest request) {
        MaintenanceRecord record = new MaintenanceRecord(
                request.droneId().trim(), request.maintenanceType().trim(), request.scheduledDate(), request.notes());
        return toResponse(repository.save(record));
    }

    @Transactional(readOnly = true)
    public List<MaintenanceResponse> forDrone(String droneId) {
        return repository.findByDroneIdOrderByScheduledDateDesc(droneId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<MaintenanceResponse> overdue() {
        return repository.findByCompletedDateIsNullAndScheduledDateBefore(LocalDate.now())
                .stream().map(this::toResponse).toList();
    }

    public MaintenanceResponse complete(Long id, String notes) {
        MaintenanceRecord record = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found: " + id));
        record.complete(notes);
        publisher.publish(FleetEvent.of("MaintenanceCompleted", "maintenance-service", record.getDroneId(),
                Map.of("maintenanceId", id, "droneId", record.getDroneId())));
        return toResponse(record);
    }

    @Scheduled(fixedDelayString = "${cloudfleet.maintenance.overdue-scan-ms:60000}")
    public void publishOverdueAlerts() {
        for (MaintenanceRecord record : repository.findByCompletedDateIsNullAndScheduledDateBeforeAndAlertSentFalse(LocalDate.now())) {
            publisher.publish(FleetEvent.of("MaintenanceDue", "maintenance-service", record.getDroneId(),
                    Map.of("maintenanceId", record.getId(), "droneId", record.getDroneId(),
                            "scheduledDate", record.getScheduledDate().toString(),
                            "maintenanceType", record.getMaintenanceType())));
            record.markAlertSent();
        }
    }

    private MaintenanceResponse toResponse(MaintenanceRecord r) {
        boolean overdue = r.getCompletedDate() == null && r.getScheduledDate().isBefore(LocalDate.now());
        return new MaintenanceResponse(r.getId(), r.getDroneId(), r.getMaintenanceType(), r.getScheduledDate(),
                r.getCompletedDate(), r.getNotes(), overdue, r.getCreatedAt());
    }
}
