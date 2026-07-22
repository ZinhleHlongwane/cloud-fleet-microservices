package com.example.cloud_fleet_microservices;

import com.example.cloud_fleet_microservices.dto.DroneDTO;
import com.example.cloud_fleet_microservices.mapper.DroneMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DroneController.class)
public class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneRepository droneRepository;

    @MockBean
    private DroneMapper droneMapper;

    @Test
    void shouldReturnBadRequestWhenBatteryIsInvalid() throws Exception {
        String invalidDroneJson = "{\"droneId\": \"TEST\", \"status\": \"IDLE\", \"battery\": 150, \"currentX\": 0, \"currentY\": 0}";

        mockMvc.perform(post("/drones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidDroneJson))
                .andExpect(status().isBadRequest());
    }
}