package com.zinhle.cloudfleet.mission.repository;

import com.zinhle.cloudfleet.mission.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByDroneIdOrderByCreatedAtDesc(String droneId);
    List<Mission> findByStatusOrderByCreatedAtDesc(MissionStatus status);
}
