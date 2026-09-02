package com.surgedispatch.service;

import com.surgedispatch.entity.Driver;
import com.surgedispatch.entity.DriverStatus;
import com.surgedispatch.entity.RideRequest;
import com.surgedispatch.entity.Rider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NearestDriverStrategyTest {

    @InjectMocks
    private NearestDriverStrategy nearestDriverStrategy;

    private RideRequest rideRequest;

    @BeforeEach
    void setUp() {
        Rider rider = new Rider("Bruce Wayne", "bruce@gotham.com", "+15550192");
        ReflectionTestUtils.setField(rider, "id", 1L);

        rideRequest = new RideRequest(rider, 26.8467, 80.9462, 26.8920, 80.9915);
        ReflectionTestUtils.setField(rideRequest, "id", 100L);
    }

    @Test
    void findMatch_shouldReturnEmpty_whenCandidateListIsEmpty() {
        Optional<Driver> match = nearestDriverStrategy.findMatch(rideRequest, Collections.emptyList());
        assertTrue(match.isEmpty());
    }

    @Test
    void findMatch_shouldReturnEmpty_whenCandidateListIsNull() {
        Optional<Driver> match = nearestDriverStrategy.findMatch(rideRequest, null);
        assertTrue(match.isEmpty());
    }

    @Test
    void findMatch_shouldSelectNearestDriver_whenMultipleOnlineDriversExist() {
        // Driver 1: ~1.5 km away from pickup (26.8467, 80.9462)
        Driver driver1 = new Driver("Clark Kent", "clark@dailyplanet.com", "+15550987", "DL-111");
        ReflectionTestUtils.setField(driver1, "id", 1L);
        driver1.setStatus(DriverStatus.ONLINE);
        driver1.setCurrentLat(26.8500);
        driver1.setCurrentLng(80.9500);

        // Driver 2: ~10 km away from pickup
        Driver driver2 = new Driver("Barry Allen", "barry@centralcity.com", "+15550988", "DL-222");
        ReflectionTestUtils.setField(driver2, "id", 2L);
        driver2.setStatus(DriverStatus.ONLINE);
        driver2.setCurrentLat(26.9300);
        driver2.setCurrentLng(81.0200);

        List<Driver> candidates = List.of(driver1, driver2);

        Optional<Driver> match = nearestDriverStrategy.findMatch(rideRequest, candidates);

        assertTrue(match.isPresent());
        assertEquals(1L, match.get().getId());
        assertEquals("Clark Kent", match.get().getName());
    }

    @Test
    void findMatch_shouldFilterOutOfflineDrivers() {
        // Driver 1: Offline but right at pickup location
        Driver driver1 = new Driver("Offline Driver", "offline@test.com", "+15550100", "DL-OFF");
        ReflectionTestUtils.setField(driver1, "id", 1L);
        driver1.setStatus(DriverStatus.OFFLINE);
        driver1.setCurrentLat(26.8467);
        driver1.setCurrentLng(80.9462);

        // Driver 2: Online but 2 km away
        Driver driver2 = new Driver("Online Driver", "online@test.com", "+15550200", "DL-ON");
        ReflectionTestUtils.setField(driver2, "id", 2L);
        driver2.setStatus(DriverStatus.ONLINE);
        driver2.setCurrentLat(26.8600);
        driver2.setCurrentLng(80.9600);

        List<Driver> candidates = List.of(driver1, driver2);

        Optional<Driver> match = nearestDriverStrategy.findMatch(rideRequest, candidates);

        assertTrue(match.isPresent());
        assertEquals(2L, match.get().getId());
        assertEquals("Online Driver", match.get().getName());
    }

    @Test
    void findMatch_shouldFilterOutDriversWithNullCoordinates() {
        Driver driverWithNullCoords = new Driver("Null Coords", "null@test.com", "+15550300", "DL-NULL");
        ReflectionTestUtils.setField(driverWithNullCoords, "id", 1L);
        driverWithNullCoords.setStatus(DriverStatus.ONLINE);

        Optional<Driver> match = nearestDriverStrategy.findMatch(rideRequest, List.of(driverWithNullCoords));

        assertTrue(match.isEmpty());
    }
}
