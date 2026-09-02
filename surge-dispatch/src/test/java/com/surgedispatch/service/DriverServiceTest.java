package com.surgedispatch.service;

import com.surgedispatch.dto.CreateDriverRequest;
import com.surgedispatch.entity.Driver;
import com.surgedispatch.exception.DriverNotFoundException;
import com.surgedispatch.exception.DuplicateEmailException;
import com.surgedispatch.exception.DuplicateLicenseNumberException;
import com.surgedispatch.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverLocationCache driverLocationCache;

    private DriverService driverService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        driverService = new DriverService(driverRepository, driverLocationCache);
    }

    @Test
    void createDriver_shouldCreateDriver() {

        // Arrange
        CreateDriverRequest request = new CreateDriverRequest(
                "Rahul",
                "+919876543210",
                "rahul@gmail.com",
                "DL12345"
        );

        Driver savedDriver = new Driver(
                "Rahul",
                "rahul@gmail.com",
                "+919876543210",
                "DL12345"
        );

        when(driverRepository.existsByLicenseNumber("DL12345"))
                .thenReturn(false);

        when(driverRepository.existsByEmail("rahul@gmail.com"))
                .thenReturn(false);

        when(driverRepository.save(any(Driver.class)))
                .thenReturn(savedDriver);

        // Act
        Driver result = driverService.createDriver(request);

        // Assert
        assertNotNull(result);
        assertEquals("Rahul", result.getName());
        assertEquals("rahul@gmail.com", result.getEmail());
        assertEquals("+919876543210", result.getPhone());
        assertEquals("DL12345", result.getLicenseNumber());

        // Verify
        verify(driverRepository).existsByLicenseNumber("DL12345");
        verify(driverRepository).existsByEmail("rahul@gmail.com");
        verify(driverRepository).save(any(Driver.class));
    }

    @Test
    void createDriver_shouldThrowException_whenLicenseNumberAlreadyExists() {

        // Arrange
        CreateDriverRequest request = new CreateDriverRequest(
                "Rahul",
                "+919876543210",
                "rahul@gmail.com",
                "DL12345"
        );

        when(driverRepository.existsByLicenseNumber("DL12345"))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                DuplicateLicenseNumberException.class,
                () -> driverService.createDriver(request)
        );

        // Verify
        verify(driverRepository).existsByLicenseNumber("DL12345");
        verify(driverRepository, never()).existsByEmail(anyString());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void createDriver_shouldThrowException_whenEmailAlreadyExists() {

        // Arrange
        CreateDriverRequest request = new CreateDriverRequest(
                "Rahul",
                "+919876543210",
                "rahul@gmail.com",
                "DL12345"
        );

        when(driverRepository.existsByLicenseNumber("DL12345"))
                .thenReturn(false);

        when(driverRepository.existsByEmail("rahul@gmail.com"))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                DuplicateEmailException.class,
                () -> driverService.createDriver(request)
        );

        // Verify
        verify(driverRepository).existsByLicenseNumber("DL12345");
        verify(driverRepository).existsByEmail("rahul@gmail.com");

        verify(driverRepository, never())
                .save(any(Driver.class));
    }

    @Test
    void getAllDrivers_shouldReturnAllDrivers() {

        // Arrange
        Driver driver1 = new Driver(
                "Rahul",
                "rahul@gmail.com",
                "+919876543210",
                "DL12345"
        );

        Driver driver2 = new Driver(
                "Amit",
                "amit@gmail.com",
                "+919876543211",
                "DL12346"
        );

        when(driverRepository.findAll())
                .thenReturn(List.of(driver1, driver2));

        // Act
        List<Driver> result = driverService.getAllDrivers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Rahul", result.get(0).getName());
        assertEquals("Amit", result.get(1).getName());

        // Verify
        verify(driverRepository).findAll();
    }

    @Test
    void getDriverById_shouldReturnDriver() {

        // Arrange
        Driver driver = new Driver(
                "Rahul",
                "rahul@gmail.com",
                "+919876543210",
                "DL12345"
        );

        when(driverRepository.findById(1L))
                .thenReturn(Optional.of(driver));

        // Act
        Driver result = driverService.getDriverById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Rahul", result.getName());
        assertEquals("rahul@gmail.com", result.getEmail());
        assertEquals("+919876543210", result.getPhone());
        assertEquals("DL12345", result.getLicenseNumber());

        // Verify
        verify(driverRepository).findById(1L);
    }

    @Test
    void getDriverById_shouldThrowException_whenDriverNotFound() {

        // Arrange
        when(driverRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                DriverNotFoundException.class,
                () -> driverService.getDriverById(1L)
        );

        // Verify
        verify(driverRepository).findById(1L);
    }

    @Test
    void deleteDriver_shouldDeleteDriver() {
        // Arrange
        Driver driver = new Driver(
                "Rahul",
                "rahul@gmail.com",
                "+919876543210",
                "DL12345"
        );
        when(driverRepository.findById(1L))
                .thenReturn(Optional.of(driver));

        // Act
        driverService.deleteDriver(1L);

        // Verify
        verify(driverRepository).findById(1L);
        verify(driverRepository).delete(driver);
        verify(driverLocationCache).removeDriver(1L);
    }
}