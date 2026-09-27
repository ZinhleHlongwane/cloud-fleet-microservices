package com.zinhle.cloudfleet.drone.repository;

import com.zinhle.cloudfleet.drone.domain.Drone;
import com.zinhle.cloudfleet.drone.domain.DroneStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, String> {

    boolean existsBySerialNumberIgnoreCase(String serialNumber);

    Page<Drone> findByStatus(DroneStatus status, Pageable pageable);

    @Query("""
            select d from Drone d
            where d.status = com.zinhle.cloudfleet.drone.domain.DroneStatus.AVAILABLE
              and d.batteryLevel >= :minimumBattery
              and d.maxPayloadKg >= :requiredPayload
            order by d.batteryLevel desc
            """)
    List<Drone> findEligibleDrones(int minimumBattery, BigDecimal requiredPayload, Pageable pageable);

    @Query("select d from Drone d where d.batteryLevel <= :threshold order by d.batteryLevel asc")
    List<Drone> findLowBattery(int threshold);
}
