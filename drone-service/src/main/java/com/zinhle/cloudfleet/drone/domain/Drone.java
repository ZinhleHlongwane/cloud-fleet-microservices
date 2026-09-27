package com.zinhle.cloudfleet.drone.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "drones")
public class Drone {

    @Id
    @Column(name = "drone_id", length = 40)
    private String droneId;

    @Column(nullable = false, unique = true, length = 80)
    private String serialNumber;

    @Column(nullable = false, length = 120)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DroneStatus status;

    @Column(nullable = false)
    private int batteryLevel;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal maxPayloadKg;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected Drone() {}

    public Drone(String droneId, String serialNumber, String model, DroneStatus status,
                 int batteryLevel, BigDecimal maxPayloadKg, Double latitude, Double longitude) {
        this.droneId = droneId;
        this.serialNumber = serialNumber;
        this.model = model;
        this.status = status;
        this.batteryLevel = batteryLevel;
        this.maxPayloadKg = maxPayloadKg;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public String getDroneId() { return droneId; }
    public String getSerialNumber() { return serialNumber; }
    public String getModel() { return model; }
    public DroneStatus getStatus() { return status; }
    public int getBatteryLevel() { return batteryLevel; }
    public BigDecimal getMaxPayloadKg() { return maxPayloadKg; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void updateDetails(String model, DroneStatus status, int batteryLevel,
                              BigDecimal maxPayloadKg, Double latitude, Double longitude) {
        this.model = model;
        this.status = status;
        this.batteryLevel = batteryLevel;
        this.maxPayloadKg = maxPayloadKg;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void changeStatus(DroneStatus status) { this.status = status; }
    public void updateBattery(int batteryLevel) { this.batteryLevel = batteryLevel; }
    public void updateLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
