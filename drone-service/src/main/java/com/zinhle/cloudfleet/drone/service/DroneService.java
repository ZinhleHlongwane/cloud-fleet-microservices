package com.zinhle.cloudfleet.drone.service;

import com.zinhle.cloudfleet.drone.api.*;
import com.zinhle.cloudfleet.drone.domain.Drone;
import com.zinhle.cloudfleet.drone.domain.DroneStatus;
import com.zinhle.cloudfleet.drone.repository.DroneRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class DroneService {

    private final DroneRepository repository;

    public DroneService(DroneRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<DroneResponse> findAll(DroneStatus status, int page, int size, String sort) {
        Sort sorting = Sort.by(sort).ascending();
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), sorting);
        Page<Drone> result = status == null ? repository.findAll(pageable) : repository.findByStatus(status, pageable);
        return result.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public DroneResponse findById(String id) {
        return toResponse(getEntity(id));
    }

    public DroneResponse create(DroneRequest request) {
        if (repository.existsById(request.droneId())) {
            throw new ConflictException("Drone ID already exists: " + request.droneId());
        }
        if (repository.existsBySerialNumberIgnoreCase(request.serialNumber())) {
            throw new ConflictException("Drone serial number already exists: " + request.serialNumber());
        }
        Drone drone = new Drone(
                request.droneId().trim(),
                request.serialNumber().trim(),
                request.model().trim(),
                request.status(),
                request.batteryLevel(),
                request.maxPayloadKg(),
                request.latitude(),
                request.longitude());
        return toResponse(repository.save(drone));
    }

    public DroneResponse update(String id, DroneUpdateRequest request) {
        Drone drone = getEntity(id);
        drone.updateDetails(request.model().trim(), request.status(), request.batteryLevel(),
                request.maxPayloadKg(), request.latitude(), request.longitude());
        return toResponse(drone);
    }

    public DroneResponse changeStatus(String id, DroneStatus status) {
        Drone drone = getEntity(id);
        drone.changeStatus(status);
        return toResponse(drone);
    }

    public void delete(String id) {
        Drone drone = getEntity(id);
        drone.changeStatus(DroneStatus.DECOMMISSIONED);
    }

    @Transactional(readOnly = true)
    public List<DroneResponse> eligible(int minimumBattery, BigDecimal payloadKg, int limit) {
        return repository.findEligibleDrones(
                minimumBattery,
                payloadKg,
                PageRequest.of(0, Math.min(limit, 50))
        ).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<DroneResponse> lowBattery(int threshold) {
        return repository.findLowBattery(threshold).stream().map(this::toResponse).toList();
    }

    private Drone getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone not found: " + id));
    }

    private DroneResponse toResponse(Drone d) {
        return new DroneResponse(d.getDroneId(), d.getSerialNumber(), d.getModel(), d.getStatus(),
                d.getBatteryLevel(), d.getMaxPayloadKg(), d.getLatitude(), d.getLongitude(),
                d.getCreatedAt(), d.getUpdatedAt());
    }
}
