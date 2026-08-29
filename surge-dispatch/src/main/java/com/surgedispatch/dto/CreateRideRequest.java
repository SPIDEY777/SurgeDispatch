package com.surgedispatch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateRideRequest {

    @NotNull(message = "Rider ID is required")
    private Long riderId;

    @NotNull(message = "Pickup latitude is required")
    private Double pickupLat;

    @NotNull(message = "Pickup longitude is required")
    private Double pickupLng;

    @NotNull(message = "Dropoff latitude is required")
    private Double dropoffLat;

    @NotNull(message = "Dropoff longitude is required")
    private Double dropoffLng;

    public CreateRideRequest(Long riderId, Double pickupLat, Double pickupLng, Double dropoffLat, Double dropoffLng) {
        this.riderId = riderId;
        this.pickupLat = pickupLat;
        this.pickupLng = pickupLng;
        this.dropoffLat = dropoffLat;
        this.dropoffLng = dropoffLng;
    }
}
