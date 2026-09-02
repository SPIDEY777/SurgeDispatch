package com.surgedispatch.controller;

import com.surgedispatch.dto.*;
import com.surgedispatch.entity.Driver;
import com.surgedispatch.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Drivers", description = "Driver account management, location telematics, and status APIs")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @Operation(summary = "Create a new driver", description = "Registers a new driver account with license number and contact details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Driver created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or duplicate email/license number")
    })
    @PostMapping("/api/v1/drivers")
    public ResponseEntity<DriverResponse> createDriver(
            @Valid @RequestBody CreateDriverRequest createDriverRequest) {

        Driver createdDriver = driverService.createDriver(createDriverRequest);
        DriverResponse response = new DriverResponse(createdDriver);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get driver by ID", description = "Retrieves driver account details by driver ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver details retrieved"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    @GetMapping("/api/v1/drivers/{id}")
    public ResponseEntity<DriverResponse> getDriverById(
            @Parameter(description = "ID of the driver to retrieve") @PathVariable Long id) {
        Driver driver = driverService.getDriverById(id);
        DriverResponse driverResponse = new DriverResponse(driver);
        return ResponseEntity.ok(driverResponse);
    }

    @Operation(summary = "Get all drivers", description = "Retrieves a list of all registered drivers.")
    @ApiResponse(responseCode = "200", description = "List of all drivers retrieved")
    @GetMapping("/api/v1/drivers")
    public ResponseEntity<List<DriverResponse>> getAllDriver() {
        List<Driver> drivers = driverService.getAllDrivers();
        List<DriverResponse> allDrivers = drivers.stream()
                .map(DriverResponse::new)
                .toList();
        return ResponseEntity.ok(allDrivers);
    }

    @Operation(summary = "Update driver", description = "Updates optional driver profile details like name, email, phone, or license number.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    @PatchMapping("/api/v1/drivers/{id}")
    public ResponseEntity<DriverResponse> updateDriver(
            @Parameter(description = "ID of the driver to update") @PathVariable Long id,
            @Valid @RequestBody UpdateDriverRequest request) {

        Driver updateDriver = driverService.updateDriver(id, request);
        DriverResponse response = new DriverResponse(updateDriver);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update driver location", description = "Receives live GPS telematics ping from driver device and updates Caffeine spatial cache.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver location updated and cached"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    @PostMapping("/api/v1/drivers/{id}/location")
    public ResponseEntity<DriverResponse> updateLocation(
            @Parameter(description = "ID of the driver sending location ping") @PathVariable Long id,
            @Valid @RequestBody DriverLocationUpdateRequest request) {

        Driver updatedDriver = driverService.updateLocation(id, request);
        return ResponseEntity.ok(new DriverResponse(updatedDriver));
    }

    @Operation(summary = "Update driver status (ONLINE/OFFLINE)", description = "Changes driver status. Going OFFLINE evicts the driver from active location cache.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Driver status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    @PostMapping("/api/v1/drivers/{id}/status")
    public ResponseEntity<DriverResponse> updateStatus(
            @Parameter(description = "ID of the driver changing status") @PathVariable Long id,
            @Valid @RequestBody DriverStatusUpdateRequest request) {

        Driver updatedDriver = driverService.updateStatus(id, request);
        return ResponseEntity.ok(new DriverResponse(updatedDriver));
    }

    @Operation(summary = "Delete driver", description = "Deletes driver account and removes driver from location cache.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Driver deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    @DeleteMapping("/api/v1/drivers/{id}")
    public ResponseEntity<Void> deleteDriver(
            @Parameter(description = "ID of the driver to delete") @PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }
}