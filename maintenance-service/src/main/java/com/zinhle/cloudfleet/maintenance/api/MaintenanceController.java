package com.zinhle.cloudfleet.maintenance.api;

import com.zinhle.cloudfleet.maintenance.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {
    private final MaintenanceService service;
    public MaintenanceController(MaintenanceService service){this.service=service;}

    @PostMapping
    public ResponseEntity<MaintenanceResponse> schedule(@Valid @RequestBody MaintenanceRequest request) {
        MaintenanceResponse created = service.schedule(request);
        return ResponseEntity.created(URI.create("/api/maintenance/" + created.id())).body(created);
    }

    @GetMapping("/drone/{droneId}")
    public List<MaintenanceResponse> forDrone(@PathVariable String droneId){return service.forDrone(droneId);}

    @GetMapping("/overdue")
    public List<MaintenanceResponse> overdue(){return service.overdue();}

    @PutMapping("/{id}/complete")
    public MaintenanceResponse complete(@PathVariable Long id, @RequestParam(required = false) String notes) {
        return service.complete(id, notes);
    }
}
