package com.example.cloud_fleet_microservices.mapper;

import com.example.cloud_fleet_microservices.Drone;
import com.example.cloud_fleet_microservices.dto.DroneDTO;
import org.springframework.stereotype.Component;

@Component
public class DroneMapper {
    public Drone toEntity(DroneDTO dto) {
        return new Drone(dto.droneId(), dto.status(), dto.battery(), dto.currentX(), dto.currentY());
    }

    public DroneDTO toDto(Drone drone) {
        return new DroneDTO(drone.getDroneId(), drone.getStatus(), drone.getBattery(), drone.getCurrentX(), drone.getCurrentY());
    }
}