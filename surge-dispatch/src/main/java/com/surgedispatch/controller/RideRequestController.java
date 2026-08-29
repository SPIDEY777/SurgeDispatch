package com.surgedispatch.controller;

import com.surgedispatch.dto.CreateRideRequest;
import com.surgedispatch.dto.RideRequestResponse;
import com.surgedispatch.entity.RideRequest;
import com.surgedispatch.service.RideRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Ride Requests", description = "Ride Request management APIs")
public class RideRequestController {

    private final RideRequestService rideRequestService;

    public RideRequestController(RideRequestService rideRequestService) {
        this.rideRequestService = rideRequestService;
    }

    @Operation(summary = "Create a new ride request")
    @PostMapping("/api/v1/ride-requests")
    public ResponseEntity<RideRequestResponse> createRideRequest(
            @Valid @RequestBody CreateRideRequest createRideRequest) {

        RideRequest createdRideRequest = rideRequestService.createRideRequest(createRideRequest);
        RideRequestResponse response = new RideRequestResponse(createdRideRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get ride request by ID")
    @GetMapping("/api/v1/ride-requests/{id}")
    public ResponseEntity<RideRequestResponse> getRideRequestById(@PathVariable Long id) {

        RideRequest rideRequest = rideRequestService.getRideRequestById(id);
        RideRequestResponse response = new RideRequestResponse(rideRequest);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all ride requests")
    @GetMapping("/api/v1/ride-requests")
    public ResponseEntity<List<RideRequestResponse>> getAllRideRequests() {

        List<RideRequest> rideRequests = rideRequestService.getAllRideRequests();
        List<RideRequestResponse> allRideRequests = rideRequests.stream()
                .map(RideRequestResponse::new)
                .toList();

        return ResponseEntity.ok(allRideRequests);
    }

    @Operation(summary = "Cancel ride request")
    @PatchMapping("/api/v1/ride-requests/{id}/cancel")
    public ResponseEntity<RideRequestResponse> cancelRideRequest(@PathVariable Long id) {

        RideRequest cancelledRideRequest = rideRequestService.cancelRideRequest(id);
        RideRequestResponse response = new RideRequestResponse(cancelledRideRequest);

        return ResponseEntity.ok(response);
    }
}
