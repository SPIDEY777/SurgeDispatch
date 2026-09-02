package com.surgedispatch.dto;

import com.surgedispatch.entity.Ride;
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
    private Long matchedDriverId;
    private String matchedDriverName;
    private Long rideId;

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

    public RideRequestResponse(RideRequest rideRequest, Ride ride) {
        this(rideRequest);
        if (ride != null) {
            this.rideId = ride.getId();
            if (ride.getDriver() != null) {
                this.matchedDriverId = ride.getDriver().getId();
                this.matchedDriverName = ride.getDriver().getName();
            }
        }
    }
}
