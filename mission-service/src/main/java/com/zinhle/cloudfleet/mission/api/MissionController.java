package com.zinhle.cloudfleet.mission.api;

import com.zinhle.cloudfleet.mission.domain.MissionStatus;
import com.zinhle.cloudfleet.mission.service.MissionService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/missions")
public class MissionController {
    private final MissionService service;

    public MissionController(MissionService service) { this.service = service; }

    @GetMapping
    public List<MissionResponse> all(@RequestParam(required = false) MissionStatus status) {
        return service.all(status);
    }

    @GetMapping("/{id}")
    public MissionResponse one(@PathVariable Long id) { return service.one(id); }

    @PostMapping
    public ResponseEntity<MissionResponse> create(@Valid @RequestBody MissionRequest request) {
        MissionResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/api/missions/" + created.id())).body(created);
    }

    @PutMapping("/{id}/assign")
    public MissionResponse assign(@PathVariable Long id) { return service.assign(id); }

    @PutMapping("/{id}/start")
    public MissionResponse start(@PathVariable Long id) { return service.start(id); }

    @PutMapping("/{id}/complete")
    public MissionResponse complete(@PathVariable Long id) { return service.complete(id); }

    @PutMapping("/{id}/fail")
    public MissionResponse fail(@PathVariable Long id) { return service.fail(id); }
}
