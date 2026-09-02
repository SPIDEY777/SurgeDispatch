package com.surgedispatch.controller;

import com.surgedispatch.dto.CreateRideRequest;
import com.surgedispatch.dto.RideRequestResponse;
import com.surgedispatch.entity.Ride;
import com.surgedispatch.entity.RideRequest;
import com.surgedispatch.service.RideRequestService;
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
@Tag(name = "Ride Requests", description = "Ride request creation, lifecycle management, and matching APIs")
public class RideRequestController {

    private final RideRequestService rideRequestService;

    public RideRequestController(RideRequestService rideRequestService) {
        this.rideRequestService = rideRequestService;
    }

    @Operation(summary = "Create a new ride request", description = "Creates a ride request and immediately triggers the matching engine to find the nearest available driver using Geohash spatial caching.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ride request created and matching executed"),
            @ApiResponse(responseCode = "400", description = "Invalid request coordinates or payload"),
            @ApiResponse(responseCode = "404", description = "Rider not found")
    })
    @PostMapping("/api/v1/ride-requests")
    public ResponseEntity<RideRequestResponse> createRideRequest(
            @Valid @RequestBody CreateRideRequest createRideRequest) {

        RideRequest createdRideRequest = rideRequestService.createRideRequest(createRideRequest);
        Ride matchedRide = rideRequestService.getRideByRideRequestId(createdRideRequest.getId()).orElse(null);

        RideRequestResponse response = new RideRequestResponse(createdRideRequest, matchedRide);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get ride request by ID", description = "Retrieves ride request details and matched driver info by ride request ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ride request retrieved"),
            @ApiResponse(responseCode = "404", description = "Ride request not found")
    })
    @GetMapping("/api/v1/ride-requests/{id}")
    public ResponseEntity<RideRequestResponse> getRideRequestById(
            @Parameter(description = "ID of the ride request") @PathVariable Long id) {

        RideRequest rideRequest = rideRequestService.getRideRequestById(id);
        Ride matchedRide = rideRequestService.getRideByRideRequestId(rideRequest.getId()).orElse(null);

        RideRequestResponse response = new RideRequestResponse(rideRequest, matchedRide);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all ride requests", description = "Retrieves a list of all ride requests in the system.")
    @ApiResponse(responseCode = "200", description = "List of all ride requests retrieved")
    @GetMapping("/api/v1/ride-requests")
    public ResponseEntity<List<RideRequestResponse>> getAllRideRequests() {

        List<RideRequest> rideRequests = rideRequestService.getAllRideRequests();
        List<RideRequestResponse> allRideRequests = rideRequests.stream()
                .map(rideRequest -> {
                    Ride matchedRide = rideRequestService.getRideByRideRequestId(rideRequest.getId()).orElse(null);
                    return new RideRequestResponse(rideRequest, matchedRide);
                })
                .toList();

        return ResponseEntity.ok(allRideRequests);
    }

    @Operation(summary = "Cancel ride request", description = "Cancels an active ride request.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ride request cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Ride request not found")
    })
    @PatchMapping("/api/v1/ride-requests/{id}/cancel")
    public ResponseEntity<RideRequestResponse> cancelRideRequest(
            @Parameter(description = "ID of the ride request to cancel") @PathVariable Long id) {

        RideRequest cancelledRideRequest = rideRequestService.cancelRideRequest(id);
        Ride matchedRide = rideRequestService.getRideByRideRequestId(cancelledRideRequest.getId()).orElse(null);

        RideRequestResponse response = new RideRequestResponse(cancelledRideRequest, matchedRide);

        return ResponseEntity.ok(response);
    }
}
