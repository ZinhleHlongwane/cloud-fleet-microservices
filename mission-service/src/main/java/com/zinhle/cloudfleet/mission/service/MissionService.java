package com.zinhle.cloudfleet.mission.service;

import com.zinhle.cloudfleet.events.FleetEvent;
import com.zinhle.cloudfleet.mission.api.*;
import com.zinhle.cloudfleet.mission.domain.*;
import com.zinhle.cloudfleet.mission.integration.DroneClient;
import com.zinhle.cloudfleet.mission.messaging.MissionEventPublisher;
import com.zinhle.cloudfleet.mission.repository.MissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MissionService {
    private final MissionRepository repository;
    private final DroneClient droneClient;
    private final MissionEventPublisher publisher;

    public MissionService(MissionRepository repository, DroneClient droneClient, MissionEventPublisher publisher) {
        this.repository = repository;
        this.droneClient = droneClient;
        this.publisher = publisher;
    }

    public MissionResponse create(MissionRequest request) {
        return toResponse(repository.save(new Mission(
                request.origin().trim(), request.destination().trim(), request.payloadKg(), request.priority())));
    }

    @Transactional(readOnly = true)
    public List<MissionResponse> all(MissionStatus status) {
        List<Mission> missions = status == null ? repository.findAll() : repository.findByStatusOrderByCreatedAtDesc(status);
        return missions.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MissionResponse one(Long id) { return toResponse(get(id)); }

    public MissionResponse assign(Long id) {
        Mission mission = get(id);
        String droneId = droneClient.chooseDrone(mission.getPayloadKg());
        mission.assign(droneId);
        droneClient.updateStatus(droneId, "ASSIGNED");
        publisher.publish(FleetEvent.of("MissionAssigned", "mission-service", String.valueOf(id),
                Map.of("missionId", id, "droneId", droneId, "priority", mission.getPriority().name())));
        return toResponse(mission);
    }

    public MissionResponse start(Long id) {
        Mission mission = get(id);
        mission.start();
        droneClient.updateStatus(mission.getDroneId(), "IN_FLIGHT");
        publisher.publish(FleetEvent.of("MissionStarted", "mission-service", String.valueOf(id),
                Map.of("missionId", id, "droneId", mission.getDroneId())));
        return toResponse(mission);
    }

    public MissionResponse complete(Long id) {
        Mission mission = get(id);
        mission.complete();
        droneClient.updateStatus(mission.getDroneId(), "AVAILABLE");
        publisher.publish(FleetEvent.of("MissionCompleted", "mission-service", String.valueOf(id),
                Map.of("missionId", id, "droneId", mission.getDroneId())));
        return toResponse(mission);
    }

    public MissionResponse fail(Long id) {
        Mission mission = get(id);
        mission.fail();
        if (mission.getDroneId() != null) droneClient.updateStatus(mission.getDroneId(), "OFFLINE");
        publisher.publish(FleetEvent.of("MissionFailed", "mission-service", String.valueOf(id),
                Map.of("missionId", id, "droneId", String.valueOf(mission.getDroneId()))));
        return toResponse(mission);
    }

    private Mission get(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mission not found: " + id));
    }

    private MissionResponse toResponse(Mission m) {
        return new MissionResponse(m.getId(), m.getDroneId(), m.getOrigin(), m.getDestination(), m.getPayloadKg(),
                m.getPriority(), m.getStatus(), m.getCreatedAt(), m.getStartedAt(), m.getCompletedAt());
    }
}
