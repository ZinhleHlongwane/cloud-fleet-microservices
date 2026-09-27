package com.zinhle.cloudfleet.alert.service;

import com.zinhle.cloudfleet.alert.api.*;
import com.zinhle.cloudfleet.alert.domain.*;
import com.zinhle.cloudfleet.alert.repository.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class AlertService {
    private final AlertRepository repository;
    public AlertService(AlertRepository repository){this.repository=repository;}

    public AlertResponse create(String eventType, String aggregateId, AlertSeverity severity, String message) {
        return toResponse(repository.save(new Alert(eventType, aggregateId, severity, message)));
    }

    @Transactional(readOnly = true)
    public List<AlertResponse> all(Boolean acknowledged) {
        List<Alert> alerts = acknowledged == null ? repository.findAll() : repository.findByAcknowledgedOrderByCreatedAtDesc(acknowledged);
        return alerts.stream().map(this::toResponse).toList();
    }

    public AlertResponse acknowledge(Long id) {
        Alert alert = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Alert not found: " + id));
        alert.acknowledge();
        return toResponse(alert);
    }

    private AlertResponse toResponse(Alert a) {
        return new AlertResponse(a.getId(), a.getEventType(), a.getAggregateId(), a.getSeverity(), a.getMessage(),
                a.isAcknowledged(), a.getCreatedAt(), a.getAcknowledgedAt());
    }
}
