package com.surgedispatch.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.surgedispatch.entity.Driver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class DriverLocationCache {

    private final Cache<Long, Driver> locationCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.SECONDS)
            .maximumSize(10_000)
            .build();
    public void updateLocation(Driver driver) {
        locationCache.put(driver.getId(), driver);
    }
    public Optional<Driver> getDriver(Long driverId) {
        return Optional.ofNullable(locationCache.getIfPresent(driverId));
    }
    public List<Driver> getAllActiveDrivers() {
        return new ArrayList<>(locationCache.asMap().values());
    }
    public void removeDriver(Long driverId) {
        locationCache.invalidate(driverId);
}
    }
