package com.surgedispatch.dto;

import com.surgedispatch.entity.RideRequest;
import com.surgedispatch.entity.RideRequestStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RideRequestResponse {

    private Long id;
    private Long riderId;
    private String riderName;
    private Double pickupLat;
    private Double pickupLng;
    private Double dropoffLat;
    private Double dropoffLng;
    private RideRequestStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime expiresAt;

    public RideRequestResponse(RideRequest rideRequest) {
        this.id = rideRequest.getId();
        this.riderId = rideRequest.getRider().getId();
        this.riderName = rideRequest.getRider().getName();
        this.pickupLat = rideRequest.getPickupLat();
        this.pickupLng = rideRequest.getPickupLng();
        this.dropoffLat = rideRequest.getDropoffLat();
        this.dropoffLng = rideRequest.getDropoffLng();
        this.status = rideRequest.getStatus();
        this.requestedAt = rideRequest.getRequestedAt();
        this.expiresAt = rideRequest.getExpiresAt();
    }
}
