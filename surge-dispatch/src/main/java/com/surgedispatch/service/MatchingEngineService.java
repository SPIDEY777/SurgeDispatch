package com.surgedispatch.service;

import com.surgedispatch.entity.*;
import com.surgedispatch.repository.DriverRepository;
import com.surgedispatch.repository.RideRepository;
import com.surgedispatch.util.GeohashUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MatchingEngineService {

    private final DriverLocationCache driverLocationCache;
    private final MatchingStrategy matchingStrategy;
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;

    public MatchingEngineService(DriverLocationCache driverLocationCache,
                                 MatchingStrategy matchingStrategy,
                                 RideRepository rideRepository,
                                 DriverRepository driverRepository) {
        this.driverLocationCache = driverLocationCache;
        this.matchingStrategy = matchingStrategy;
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
    }

    @Transactional
    public Optional<Ride> matchAndDispatch(RideRequest rideRequest) {
        if (rideRequest == null || rideRequest.getStatus() != RideRequestStatus.REQUESTED) {
            return Optional.empty();
        }

        List<String> searchGeohashes = GeohashUtils.getSearchGeohashes(
                rideRequest.getPickupLat(),
                rideRequest.getPickupLng()
        );

        List<Driver> candidateDrivers = driverLocationCache.getCandidateDriversInGeohashes(searchGeohashes);
        if (candidateDrivers.isEmpty()) {
            candidateDrivers = driverLocationCache.getAllActiveDrivers();
        }

        Optional<Driver> matchedDriverOpt = matchingStrategy.findMatch(rideRequest, candidateDrivers);

        if (matchedDriverOpt.isEmpty()) {
            return Optional.empty();
        }

        Driver matchedDriver = matchedDriverOpt.get();

        // Update driver status & invalidate location cache
        matchedDriver.setStatus(DriverStatus.EN_ROUTE);
        driverRepository.save(matchedDriver);
        driverLocationCache.removeDriver(matchedDriver.getId());

        // Update ride request status
        rideRequest.setStatus(RideRequestStatus.MATCHED);

        // Create ride entity (Default base price 15.0, multiplier 1.0)
        Ride ride = new Ride(rideRequest, matchedDriver, 15.0, 1.0, 15.0);
        Ride savedRide = rideRepository.save(ride);

        return Optional.of(savedRide);
    }
}
