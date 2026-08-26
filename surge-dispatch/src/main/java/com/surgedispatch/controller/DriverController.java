package com.surgedispatch.controller;

import com.surgedispatch.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.surgedispatch.entity.Driver;



import com.surgedispatch.service.DriverService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@Tag(name = "Drivers", description = "Driver management APIs")
public class DriverController {


    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }
    @Operation(summary = "Create a new driver")
    @PostMapping("/api/v1/drivers")
    public ResponseEntity<DriverResponse> createDriver(
            @Valid @RequestBody CreateDriverRequest createDriverRequest) {


        Driver createdDriver = driverService.createDriver(createDriverRequest);

        DriverResponse response = new DriverResponse(createdDriver);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @Operation(summary = "Get driver by ID")
    @GetMapping("/api/v1/drivers/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id) {

        Driver driver = driverService.getDriverById(id);
        DriverResponse driverResponse = new DriverResponse((driver));

        return ResponseEntity.ok(driverResponse);

    }
    @Operation(summary = "Get all drivers")
    @GetMapping("/api/v1/drivers")
    public ResponseEntity<List<DriverResponse>> getAllDriver() {

        List<Driver> drivers = driverService.getAllDrivers();

        List<DriverResponse> allDrivers = drivers.stream()
                .map(DriverResponse::new)
                .toList();

        return ResponseEntity.ok(allDrivers);
    }
    @Operation(summary = "Update driver")
    @PatchMapping("/api/v1/drivers/{id}")
    public ResponseEntity<DriverResponse> updateDriver(@PathVariable Long id
            ,@Valid @RequestBody UpdateDriverRequest request) {

        Driver updateDriver = driverService.updateDriver(id,request);


        DriverResponse response = new DriverResponse((updateDriver));

        return ResponseEntity.ok(response);

    }
    @Operation(summary = "Delete driver")
    @DeleteMapping("/api/v1/drivers/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {

        driverService.deleteDriver(id);

        return ResponseEntity.noContent().build();
    }
}