package com.zinhle.cloudfleet.mission.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MissionTest {
    private Mission mission() {
        return new Mission("Sandton","Midrand",new BigDecimal("2.5"),MissionPriority.HIGH);
    }

    @Test void startsPlanned(){assertEquals(MissionStatus.PLANNED, mission().getStatus());}

    @Test void assignmentMovesToAssigned(){
        Mission m=mission(); m.assign("D1");
        assertEquals(MissionStatus.ASSIGNED,m.getStatus());
        assertEquals("D1",m.getDroneId());
    }

    @Test void cannotStartBeforeAssignment(){
        assertThrows(IllegalArgumentException.class, () -> mission().start());
    }

    @Test void fullLifecycleCompletes(){
        Mission m=mission(); m.assign("D1"); m.start(); m.complete();
        assertEquals(MissionStatus.COMPLETED,m.getStatus());
        assertNotNull(m.getCompletedAt());
    }

    @Test void completedMissionCannotFail(){
        Mission m=mission(); m.assign("D1"); m.start(); m.complete();
        assertThrows(IllegalArgumentException.class, m::fail);
    }
}
