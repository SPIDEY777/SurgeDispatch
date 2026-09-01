package com.surgedispatch.service;

import com.surgedispatch.entity.Driver;
import com.surgedispatch.entity.DriverStatus;
import com.surgedispatch.entity.RideRequest;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class NearestDriverStrategy implements MatchingStrategy {

    @Override
    public Optional<Driver> findMatch(RideRequest request, List<Driver> candidateDrivers) {
        if (candidateDrivers == null || candidateDrivers.isEmpty()) {
            return Optional.empty();
        }

        return candidateDrivers.stream()
                .filter(driver -> driver.getCurrentLat() != null
                        && driver.getCurrentLng() != null
                        && driver.getStatus() == DriverStatus.ONLINE)
                .min(Comparator.comparingDouble(driver -> calculateDistance(
                        request.getPickupLat(), request.getPickupLng(),
                        driver.getCurrentLat(), driver.getCurrentLng()
                )));
    }
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371 * c; // Radius of Earth in KM
    }
}
