package com.zinhle.cloudfleet.maintenance.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class MaintenanceRecordTest {
    @Test void completionStoresDate() {
        MaintenanceRecord record = new MaintenanceRecord("D1","inspection",LocalDate.now(),"initial");
        record.complete("done");
        assertEquals(LocalDate.now(), record.getCompletedDate());
        assertEquals("done", record.getNotes());
    }

    @Test void alertCanBeMarkedSent() {
        MaintenanceRecord record = new MaintenanceRecord("D1","inspection",LocalDate.now(),"");
        assertFalse(record.isAlertSent());
        record.markAlertSent();
        assertTrue(record.isAlertSent());
    }
}
