package com.example.cloud_fleet_microservices;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;


@Entity // This tells Spring that this class represents a permanent SQL table
@Table(name = "drones") // Tells PostgreSQL to name the table "drones"
public class Drone {

    @Id //Every table needs a unique identifier. This enforces the Primary Key constraint
    @Column(name = "drone_id") // This flags droneId as the Primary Key (PK) in the database
    private String droneId;

    @Column(nullable = false) // Column let's us customize how the column look in PostgreSQL
    private String status;

    @Column(nullable = false)
    private int battery;

    @Column(name = "current_x")
    private int currentX;

    @Column(name = "current_y")
    private int currentY;

    // Default constructor (Spring Boot needs this empty one to function properly later)
    public Drone() {}

    // Constructor to quickly build a new drone object in the code
    public Drone(String droneId, String status, int battery, int currentX, int currentY) {
        this.droneId = droneId;
        this.status = status;
        this.battery = battery;
        this.currentX = currentX;
        this.currentY = currentY;
    }

    // Getter methods
    public String getDroneId() { return droneId; }
    public String getStatus() { return status; }
    public int getBattery() { return battery; }
    public int getCurrentX() { return currentX; }
    public int getCurrentY() { return currentY; }

    // Setter methods
    public void setDroneId(String droneId) { this.droneId = droneId; }
    public void setStatus(String status) { this.status = status; }
    public void setBattery(int battery) { this.battery = battery; }
    public void setCurrentX(int currentX) { this.currentX = currentX; }
    public void setCurrentY(int currentY) { this.currentY = currentY; }
}