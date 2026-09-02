package com.surgedispatch.controller;

import com.surgedispatch.dto.CreateRiderRequest;
import com.surgedispatch.dto.RiderResponse;
import com.surgedispatch.dto.UpdateRiderRequest;
import com.surgedispatch.entity.Rider;
import com.surgedispatch.service.RiderService;
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

@Tag(name = "Riders", description = "Rider profile and account management APIs")
@RestController
public class RiderController {

    private final RiderService riderService;

    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    @Operation(summary = "Create a new rider", description = "Registers a new rider profile with contact details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rider created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payload or duplicate email")
    })
    @PostMapping("/api/v1/riders")
    public ResponseEntity<RiderResponse> createRider(
            @Valid @RequestBody CreateRiderRequest createRiderRequest) {

        Rider createdRider = riderService.createRider(createRiderRequest);
        RiderResponse response = new RiderResponse(createdRider);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get rider by ID", description = "Retrieves rider account profile by rider ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rider profile retrieved"),
            @ApiResponse(responseCode = "404", description = "Rider not found")
    })
    @GetMapping("/api/v1/riders/{id}")
    public ResponseEntity<RiderResponse> getRider(
            @Parameter(description = "ID of the rider to retrieve") @PathVariable Long id) {

        Rider rider = riderService.getRiderById(id);
        RiderResponse response = new RiderResponse(rider);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all riders", description = "Retrieves a list of all registered riders.")
    @ApiResponse(responseCode = "200", description = "List of all riders retrieved")
    @GetMapping("/api/v1/riders")
    public ResponseEntity<List<RiderResponse>> getAllRider() {

        List<Rider> riders = riderService.getAllRiders();
        List<RiderResponse> allRiders = riders.stream()
                .map(RiderResponse::new)
                .toList();

        return ResponseEntity.ok(allRiders);
    }

    @Operation(summary = "Update rider", description = "Updates rider profile fields like name, email, or phone.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rider profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Rider not found")
    })
    @PatchMapping("/api/v1/riders/{id}")
    public ResponseEntity<RiderResponse> updateRider(
            @Parameter(description = "ID of the rider to update") @PathVariable Long id,
            @Valid @RequestBody UpdateRiderRequest request) {

        Rider updatedRider = riderService.updateRider(id, request);
        RiderResponse response = new RiderResponse(updatedRider);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete rider", description = "Deletes a rider profile by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Rider deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Rider not found")
    })
    @DeleteMapping("/api/v1/riders/{id}")
    public ResponseEntity<Void> deleteRider(
            @Parameter(description = "ID of the rider to delete") @PathVariable Long id) {

        riderService.deleteRider(id);
        return ResponseEntity.noContent().build();
    }
}
