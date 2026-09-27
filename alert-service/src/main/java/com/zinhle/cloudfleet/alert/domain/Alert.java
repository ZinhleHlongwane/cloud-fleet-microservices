package com.zinhle.cloudfleet.alert.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "alerts")
public class Alert {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 60)
    private String eventType;
    @Column(length = 80)
    private String aggregateId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertSeverity severity;
    @Column(nullable = false, length = 1000)
    private String message;
    @Column(nullable = false)
    private boolean acknowledged;
    @Column(nullable = false)
    private Instant createdAt;
    private Instant acknowledgedAt;

    protected Alert(){}

    public Alert(String eventType, String aggregateId, AlertSeverity severity, String message) {
        this.eventType=eventType; this.aggregateId=aggregateId; this.severity=severity; this.message=message;
        this.createdAt=Instant.now();
    }
    public Long getId(){return id;}
    public String getEventType(){return eventType;}
    public String getAggregateId(){return aggregateId;}
    public AlertSeverity getSeverity(){return severity;}
    public String getMessage(){return message;}
    public boolean isAcknowledged(){return acknowledged;}
    public Instant getCreatedAt(){return createdAt;}
    public Instant getAcknowledgedAt(){return acknowledgedAt;}
    public void acknowledge(){this.acknowledged=true; this.acknowledgedAt=Instant.now();}
}
