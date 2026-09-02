package com.surgedispatch.service;

import com.surgedispatch.entity.Driver;
import com.surgedispatch.entity.DriverStatus;
import com.surgedispatch.util.GeohashUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DriverLocationCacheTest {

    private DriverLocationCache driverLocationCache;

    @BeforeEach
    void setUp() {
        driverLocationCache = new DriverLocationCache();
    }

    @Test
    void updateLocation_shouldStoreDriverInCacheAndGeohashBucket() {
        Driver driver = new Driver("Clark Kent", "clark@dailyplanet.com", "+15550987", "DL-111");
        ReflectionTestUtils.setField(driver, "id", 1L);
        driver.setStatus(DriverStatus.ONLINE);
        driver.setCurrentLat(26.8467);
        driver.setCurrentLng(80.9462);

        driverLocationCache.updateLocation(driver);

        Optional<Driver> cachedDriver = driverLocationCache.getDriver(1L);
        assertTrue(cachedDriver.isPresent());
        assertEquals("Clark Kent", cachedDriver.get().getName());

        List<String> searchGeohashes = GeohashUtils.getSearchGeohashes(26.8467, 80.9462);
        List<Driver> candidates = driverLocationCache.getCandidateDriversInGeohashes(searchGeohashes);

        assertFalse(candidates.isEmpty());
        assertEquals(1L, candidates.get(0).getId());
    }

    @Test
    void removeDriver_shouldInvalidateDriverFromCache() {
        Driver driver = new Driver("Barry Allen", "barry@centralcity.com", "+15550988", "DL-222");
        ReflectionTestUtils.setField(driver, "id", 2L);
        driver.setStatus(DriverStatus.ONLINE);
        driver.setCurrentLat(26.8500);
        driver.setCurrentLng(80.9500);

        driverLocationCache.updateLocation(driver);
        driverLocationCache.removeDriver(2L);

        Optional<Driver> cachedDriver = driverLocationCache.getDriver(2L);
        assertTrue(cachedDriver.isEmpty());
    }
}
