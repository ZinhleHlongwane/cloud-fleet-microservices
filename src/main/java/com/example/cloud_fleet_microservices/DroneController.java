package com.example.cloud_fleet_microservices;

import com.example.cloud_fleet_microservices.dto.DroneDTO;
import com.example.cloud_fleet_microservices.mapper.DroneMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.cloud_fleet_microservices.DroneRepository;
import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController // Tells Spring that this class will handle web requests and return data in JSON
@RequestMapping("/drones") // Sets the base URL path to http://localhost:8080/drones
public class DroneController {

    private final DroneRepository droneRepository;
    private final DroneMapper droneMapper;

    public DroneController(DroneRepository droneRepository, DroneMapper droneMapper) {
        this.droneRepository = droneRepository;
        this.droneMapper = droneMapper;
    }

    // GET all drones
    @GetMapping // When visiting the URL, it calls the findAll() and sends the list of drones 
    public List<DroneDTO> getAllDrones() {
        return droneRepository.findAll().stream()
                .map(droneMapper::toDto)
                .collect(Collectors.toList());
    }

    // POST a new drone
    @PostMapping // When sending data to this URL, it calls save() and puts the new drone into the H2 database
    public ResponseEntity<DroneDTO> createDrone(@Valid @RequestBody DroneDTO droneDto) {
        Drone drone = droneMapper.toEntity(droneDto);
        Drone savedDrone = droneRepository.save(drone);
        return ResponseEntity.ok(droneMapper.toDto(savedDrone));
    }
}