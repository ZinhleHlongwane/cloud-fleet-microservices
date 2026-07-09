package com.example.cloud_fleet_microservices;

public class Drone {

    private String droneId;
    private String status;
    private int battery;
    private int currentX;
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