package com.example.cloud_fleet_microservices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRepository extends JpaRepository<Drone, String> {
    // That's it!
    // You now have .save(), .findAll(), .findById(), and .delete()
    // ready to use in your Java code automatically.
}