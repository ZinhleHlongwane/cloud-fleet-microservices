package com.zinhle.cloudfleet.mission.integration;

import com.zinhle.cloudfleet.mission.api.ConflictException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DroneClient {
    private final RestClient client;

    public DroneClient(RestClient.Builder builder, @Value("${cloudfleet.drone-service-url}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    public String chooseDrone(BigDecimal payloadKg) {
        DroneSummary[] drones = client.get()
                .uri(uri -> uri.path("/api/drones/eligible")
                        .queryParam("minimumBattery", 30)
                        .queryParam("payloadKg", payloadKg)
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .body(DroneSummary[].class);
        if (drones == null || drones.length == 0) {
            throw new ConflictException("No eligible drone is available for payload " + payloadKg + " kg");
        }
        return drones[0].droneId();
    }

    public void updateStatus(String droneId, String status) {
        client.patch()
                .uri("/api/drones/{id}/status?status={status}", droneId, status)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new IllegalStateException("Drone service rejected status update: " + res.getStatusCode());
                })
                .toBodilessEntity();
    }

    public record DroneSummary(String droneId) {}
}
