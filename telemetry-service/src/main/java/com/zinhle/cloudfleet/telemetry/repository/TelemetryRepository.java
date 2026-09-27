package com.zinhle.cloudfleet.telemetry.repository;

import com.zinhle.cloudfleet.telemetry.domain.TelemetrySample;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TelemetryRepository extends JpaRepository<TelemetrySample, Long> {
    Optional<TelemetrySample> findFirstByDroneIdOrderByRecordedAtDesc(String droneId);
    List<TelemetrySample> findByDroneIdOrderByRecordedAtDesc(String droneId, Pageable pageable);
}
