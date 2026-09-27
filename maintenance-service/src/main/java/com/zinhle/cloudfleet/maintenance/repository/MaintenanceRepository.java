package com.zinhle.cloudfleet.maintenance.repository;

import com.zinhle.cloudfleet.maintenance.domain.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord, Long> {
    List<MaintenanceRecord> findByDroneIdOrderByScheduledDateDesc(String droneId);
    List<MaintenanceRecord> findByCompletedDateIsNullAndScheduledDateBefore(LocalDate date);
    List<MaintenanceRecord> findByCompletedDateIsNullAndScheduledDateBeforeAndAlertSentFalse(LocalDate date);
}
