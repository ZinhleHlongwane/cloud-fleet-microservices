package com.zinhle.cloudfleet.telemetry.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "telemetry_samples")
public class TelemetrySample {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 40)
    private String droneId;
    @Column(nullable = false)
    private int batteryLevel;
    private Double latitude;
    private Double longitude;
    private Double altitudeMeters;
    private Double speedKph;
    @Column(nullable = false)
    private Instant recordedAt;

    protected TelemetrySample(){}

    public TelemetrySample(String droneId, int batteryLevel, Double latitude, Double longitude,
                           Double altitudeMeters, Double speedKph, Instant recordedAt) {
        this.droneId=droneId; this.batteryLevel=batteryLevel; this.latitude=latitude; this.longitude=longitude;
        this.altitudeMeters=altitudeMeters; this.speedKph=speedKph;
        this.recordedAt=recordedAt == null ? Instant.now() : recordedAt;
    }

    public Long getId(){return id;}
    public String getDroneId(){return droneId;}
    public int getBatteryLevel(){return batteryLevel;}
    public Double getLatitude(){return latitude;}
    public Double getLongitude(){return longitude;}
    public Double getAltitudeMeters(){return altitudeMeters;}
    public Double getSpeedKph(){return speedKph;}
    public Instant getRecordedAt(){return recordedAt;}
}
