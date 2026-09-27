package com.zinhle.cloudfleet.drone.api;

import com.zinhle.cloudfleet.drone.domain.DroneStatus;
import com.zinhle.cloudfleet.drone.service.DroneService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/drones")
public class DroneController {

    private final DroneService service;

    public DroneController(DroneService service) {
        this.service = service;
    }

    @GetMapping
    public Page<DroneResponse> all(
            @RequestParam(name = "status", required = false) DroneStatus status,
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(name = "sort", defaultValue = "droneId") String sort) {

        return service.findAll(status, page, size, sort);
    }

    @GetMapping("/{id}")
    public DroneResponse one(
            @PathVariable(name = "id") String id) {

        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<DroneResponse> create(
            @Valid @RequestBody DroneRequest request) {

        DroneResponse created = service.create(request);

        return ResponseEntity
                .created(URI.create("/api/drones/" + created.droneId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public DroneResponse update(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody DroneUpdateRequest request) {

        return service.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public DroneResponse changeStatus(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "status") DroneStatus status) {

        return service.changeStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void decommission(
            @PathVariable(name = "id") String id) {

        service.delete(id);
    }

    @GetMapping("/eligible")
    public List<DroneResponse> eligible(
            @RequestParam(name = "minimumBattery", defaultValue = "30")
            @Min(0) @Max(100) int minimumBattery,

            @RequestParam(name = "payloadKg")
            @DecimalMin(value = "0.1") BigDecimal payloadKg,

            @RequestParam(name = "limit", defaultValue = "10")
            @Min(1) @Max(50) int limit) {

        return service.eligible(minimumBattery, payloadKg, limit);
    }

    @GetMapping("/low-battery")
    public List<DroneResponse> lowBattery(
            @RequestParam(name = "threshold", defaultValue = "20")
            @Min(0) @Max(100) int threshold) {

        return service.lowBattery(threshold);
    }
}