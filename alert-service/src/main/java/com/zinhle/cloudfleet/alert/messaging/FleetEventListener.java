package com.zinhle.cloudfleet.alert.messaging;

import com.zinhle.cloudfleet.alert.domain.AlertSeverity;
import com.zinhle.cloudfleet.alert.service.AlertService;
import com.zinhle.cloudfleet.events.FleetEvent;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class FleetEventListener {
    private final AlertService service;
    public FleetEventListener(AlertService service){this.service=service;}

    @JmsListener(destination = "fleet.events.queue")
    public void consume(FleetEvent event) {
        AlertSeverity severity = switch (event.eventType()) {
            case "MissionFailed", "DroneOffline" -> AlertSeverity.CRITICAL;
            case "BatteryLow", "MaintenanceDue", "InvalidTelemetry" -> AlertSeverity.WARNING;
            default -> AlertSeverity.INFO;
        };
        String message = event.eventType() + " from " + event.sourceService() + " for " + event.aggregateId()
                + " data=" + event.data();
        service.create(event.eventType(), event.aggregateId(), severity, message);
    }
}
