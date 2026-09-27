package com.zinhle.cloudfleet.maintenance.domain;

import jakarta.persistence.*;

import java.time.*;

@Entity
@Table(name = "maintenance_records")
public class MaintenanceRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String droneId;

    @Column(nullable = false, length = 80)
    private String maintenanceType;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    private LocalDate completedDate;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private boolean alertSent;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected MaintenanceRecord() {}

    public MaintenanceRecord(String droneId, String maintenanceType, LocalDate scheduledDate, String notes) {
        this.droneId = droneId;
        this.maintenanceType = maintenanceType;
        this.scheduledDate = scheduledDate;
        this.notes = notes;
    }

    @PrePersist void prePersist() { createdAt = Instant.now(); }

    public Long getId(){return id;}
    public String getDroneId(){return droneId;}
    public String getMaintenanceType(){return maintenanceType;}
    public LocalDate getScheduledDate(){return scheduledDate;}
    public LocalDate getCompletedDate(){return completedDate;}
    public String getNotes(){return notes;}
    public boolean isAlertSent(){return alertSent;}
    public Instant getCreatedAt(){return createdAt;}

    public void complete(String notes) {
        this.completedDate = LocalDate.now();
        if (notes != null && !notes.isBlank()) this.notes = notes;
    }
    public void markAlertSent(){this.alertSent = true;}
}
