package com.surgedispatch.service;

import com.surgedispatch.entity.Driver;
import com.surgedispatch.entity.RideRequest;


import java.util.List;
import java.util.Optional;

public interface MatchingStrategy {
    Optional<Driver> findMatch(
            RideRequest request,
            List<Driver>  candidateDrivers
    );
}
