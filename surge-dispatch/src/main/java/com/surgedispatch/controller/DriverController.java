package com.surgedispatch.controller;

import com.surgedispatch.dto.CreateDriverRequest;

import com.surgedispatch.dto.DriverResponse;


import com.surgedispatch.entity.Driver;


import com.surgedispatch.service.DriverService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DriverController {



    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/api/v1/drivers")
    public ResponseEntity<DriverResponse> createDriver(
            @Valid @RequestBody CreateDriverRequest createDriverRequest) {


        Driver createdDriver = driverService.createDriver(createDriverRequest);

        DriverResponse response = new DriverResponse(createdDriver);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/api/v1/drivers/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id){

Driver driver = driverService.getDriverById(id);
DriverResponse driverResponse = new DriverResponse((driver));

return  ResponseEntity.ok(driverResponse);

    }
    @GetMapping("/api/v1/drivers")
    public ResponseEntity<List<DriverResponse>> getAllDriver(){

        List<Driver> drivers =  driverService.getAllDrivers();

        List<DriverResponse> allDrivers = drivers.stream()
                .map(DriverResponse::new)
                .toList();

        return ResponseEntity.ok(allDrivers);
    }
}