package com.zinhle.cloudfleet.mission.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "missions")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String droneId;

    @Column(nullable = false, length = 160)
    private String origin;

    @Column(nullable = false, length = 160)
    private String destination;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal payloadKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant startedAt;
    private Instant completedAt;

    protected Mission() {}

    public Mission(String origin, String destination, BigDecimal payloadKg, MissionPriority priority) {
        this.origin = origin;
        this.destination = destination;
        this.payloadKg = payloadKg;
        this.priority = priority;
        this.status = MissionStatus.PLANNED;
    }

    @PrePersist
    void prePersist() { createdAt = Instant.now(); }

    public Long getId() { return id; }
    public String getDroneId() { return droneId; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public BigDecimal getPayloadKg() { return payloadKg; }
    public MissionPriority getPriority() { return priority; }
    public MissionStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }

    public void assign(String droneId) {
        requireStatus(MissionStatus.PLANNED);
        this.droneId = droneId;
        this.status = MissionStatus.ASSIGNED;
    }

    public void start() {
        requireStatus(MissionStatus.ASSIGNED);
        status = MissionStatus.IN_PROGRESS;
        startedAt = Instant.now();
    }

    public void complete() {
        requireStatus(MissionStatus.IN_PROGRESS);
        status = MissionStatus.COMPLETED;
        completedAt = Instant.now();
    }

    public void fail() {
        if (status == MissionStatus.COMPLETED || status == MissionStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot fail mission in status " + status);
        }
        status = MissionStatus.FAILED;
        completedAt = Instant.now();
    }

    public void cancel() {
        if (status == MissionStatus.COMPLETED) {
            throw new IllegalArgumentException("Completed missions cannot be cancelled");
        }
        status = MissionStatus.CANCELLED;
        completedAt = Instant.now();
    }

    private void requireStatus(MissionStatus expected) {
        if (status != expected) {
            throw new IllegalArgumentException("Mission must be " + expected + " but is " + status);
        }
    }
}
