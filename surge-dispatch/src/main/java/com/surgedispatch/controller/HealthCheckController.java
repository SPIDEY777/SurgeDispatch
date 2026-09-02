package com.surgedispatch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Tag(name = "Health Check", description = "System health and status check APIs")
public class HealthCheckController {

    @Operation(summary = "System health check", description = "Returns system operational status, timestamp, and service info.")
    @ApiResponse(responseCode = "200", description = "System is healthy and operational")
    @GetMapping("/api/v1/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> healthInfo = Map.of(
                "status", "UP",
                "service", "SurgeDispatch",
                "timestamp", LocalDateTime.now()
        );
        return ResponseEntity.ok(healthInfo);
    }
}
