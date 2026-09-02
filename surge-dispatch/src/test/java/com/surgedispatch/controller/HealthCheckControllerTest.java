package com.surgedispatch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HealthCheckControllerTest {

    @Test
    void healthCheck_shouldReturnStatusUp() {
        HealthCheckController controller = new HealthCheckController();
        ResponseEntity<Map<String, Object>> response = controller.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals("SurgeDispatch", response.getBody().get("service"));
        assertNotNull(response.getBody().get("timestamp"));
    }
}
