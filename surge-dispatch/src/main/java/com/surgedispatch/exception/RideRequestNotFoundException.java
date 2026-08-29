package com.surgedispatch.exception;

public class RideRequestNotFoundException extends RuntimeException {

    public RideRequestNotFoundException(Long id) {
        super("Ride request not found with ID: " + id);
    }
}
