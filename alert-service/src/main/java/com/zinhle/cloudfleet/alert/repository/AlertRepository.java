package com.zinhle.cloudfleet.alert.repository;

import com.zinhle.cloudfleet.alert.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByAcknowledgedOrderByCreatedAtDesc(boolean acknowledged);
}
