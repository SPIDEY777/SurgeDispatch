package com.surgedispatch.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.surgedispatch.entity.Driver;
import com.surgedispatch.util.GeohashUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class DriverLocationCache {

    private final Cache<Long, Driver> driverCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.SECONDS)
            .maximumSize(10_000)
            .build();

    private final Cache<String, Set<Long>> geohashCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.SECONDS)
            .maximumSize(50_000)
            .build();

    public void updateLocation(Driver driver) {
        if (driver == null || driver.getId() == null || driver.getCurrentLat() == null || driver.getCurrentLng() == null) {
            return;
        }

        driverCache.put(driver.getId(), driver);

        String geohash = GeohashUtils.encode(driver.getCurrentLat(), driver.getCurrentLng());
        Set<Long> driversInBucket = geohashCache.get(geohash, k -> ConcurrentHashMap.newKeySet());
        if (driversInBucket != null) {
            driversInBucket.add(driver.getId());
        }
    }

    public Optional<Driver> getDriver(Long driverId) {
        return Optional.ofNullable(driverCache.getIfPresent(driverId));
    }

    public List<Driver> getCandidateDriversInGeohashes(List<String> geohashPrefixes) {
        Set<Long> driverIds = new HashSet<>();

        for (String geohash : geohashPrefixes) {
            Set<Long> bucket = geohashCache.getIfPresent(geohash);
            if (bucket != null) {
                driverIds.addAll(bucket);
            }
        }

        List<Driver> candidates = new ArrayList<>();
        for (Long driverId : driverIds) {
            Driver driver = driverCache.getIfPresent(driverId);
            if (driver != null) {
                candidates.add(driver);
            }
        }

        return candidates;
    }

    public List<Driver> getAllActiveDrivers() {
        return new ArrayList<>(driverCache.asMap().values());
    }

    public void removeDriver(Long driverId) {
        Driver driver = driverCache.getIfPresent(driverId);
        if (driver != null && driver.getCurrentLat() != null && driver.getCurrentLng() != null) {
            String geohash = GeohashUtils.encode(driver.getCurrentLat(), driver.getCurrentLng());
            Set<Long> bucket = geohashCache.getIfPresent(geohash);
            if (bucket != null) {
                bucket.remove(driverId);
            }
        }
        driverCache.invalidate(driverId);
    }
}
