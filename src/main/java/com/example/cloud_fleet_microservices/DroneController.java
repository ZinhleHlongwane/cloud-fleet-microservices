package com.example.cloud_fleet_microservices;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Tells Spring that this class will handle web requests and return data in JSON
@RequestMapping("/drones") // Sets the base URL path to http://localhost:8080/drones
public class DroneController {

    private final DroneRepository droneRepository;

    public DroneController(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    // GET all drones
    @GetMapping // When visiting the URL, it calls the findAll() and sends the list of drones 
    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    // POST a new drone
    @PostMapping // When sending data to this URL, it calls save() and puts the new drone into the H2 database
    public Drone createDrone(@RequestBody Drone drone) {
        return droneRepository.save(drone);
    }
}