package com.example.cloud_fleet_microservices.dto;

import jakarta.validation.constraints.*;

public record DroneDTO(
    @NotBlank(message = "Drone ID cannot be empty") String droneId,
    @NotBlank(message = "Status cannot be empty") String status,
    @Min(value = 0, message = "Battery cannot be less than 0")
    @Max(value = 100, message = "Battery cannot be more than 100") int battery,
    int currentX,
    int currentY
) {}