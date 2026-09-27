package com.zinhle.cloudfleet.drone.api;

import com.zinhle.cloudfleet.drone.domain.DroneStatus;
import com.zinhle.cloudfleet.drone.service.DroneService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
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
            @RequestParam(required = false) DroneStatus status,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "droneId") String sort) {
        return service.findAll(status, page, size, sort);
    }

    @GetMapping("/{id}")
    public DroneResponse one(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<DroneResponse> create(@Valid @RequestBody DroneRequest request) {
        DroneResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/api/drones/" + created.droneId())).body(created);
    }

    @PutMapping("/{id}")
    public DroneResponse update(@PathVariable String id, @Valid @RequestBody DroneUpdateRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public DroneResponse changeStatus(@PathVariable String id, @RequestParam DroneStatus status) {
        return service.changeStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void decommission(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/eligible")
    public List<DroneResponse> eligible(
            @RequestParam(defaultValue = "30") @Min(0) @Max(100) int minimumBattery,
            @RequestParam @DecimalMin(value = "0.1") BigDecimal payloadKg,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int limit) {
        return service.eligible(minimumBattery, payloadKg, limit);
    }

    @GetMapping("/low-battery")
    public List<DroneResponse> lowBattery(@RequestParam(defaultValue = "20") @Min(0) @Max(100) int threshold) {
        return service.lowBattery(threshold);
    }
}
