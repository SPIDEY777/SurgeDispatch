package com.surgedispatch.service;

import com.surgedispatch.dto.CreateDriverRequest;
import com.surgedispatch.dto.UpdateDriverRequest;
import com.surgedispatch.entity.Driver;
import com.surgedispatch.exception.DriverNotFoundException;
import com.surgedispatch.exception.DuplicateEmailException;
import com.surgedispatch.exception.DuplicateLicenseNumberException;
import com.surgedispatch.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Transactional
    public Driver createDriver(CreateDriverRequest createDriverRequest) {
        if (driverRepository.existsByLicenseNumber(createDriverRequest.getLicenseNumber())) {
            throw new DuplicateLicenseNumberException("A driver with this license number already exists.");
        }
        if (driverRepository.existsByEmail(createDriverRequest.getEmail())) {
            throw new DuplicateEmailException("A driver with this email already exists.");
        }

        Driver driver = new Driver(
                createDriverRequest.getName(),
                createDriverRequest.getEmail(),
                createDriverRequest.getPhone(),
                createDriverRequest.getLicenseNumber()
        );
        return driverRepository.save(driver);
    }

    @Transactional(readOnly = true)
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Transactional
    public Driver updateDriver(Long id, UpdateDriverRequest request) {
        Driver existingDriver = getDriverById(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            existingDriver.setName(request.getName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!existingDriver.getEmail().equals(request.getEmail()) &&
                    driverRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateEmailException("A driver with this email already exists.");
            }
            existingDriver.setEmail(request.getEmail());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            existingDriver.setPhone(request.getPhone());
        }
        if (request.getLicenseNumber() != null && !request.getLicenseNumber().isBlank()) {
            if (!existingDriver.getLicenseNumber().equals(request.getLicenseNumber()) &&
                    driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
                throw new DuplicateLicenseNumberException("A driver with this license number already exists.");
            }
            existingDriver.setLicenseNumber(request.getLicenseNumber());
        }

        return driverRepository.save(existingDriver);
    }

    @Transactional
    public void deleteDriver(Long id) {
        Driver driver = getDriverById(id);
        driverRepository.delete(driver);
    }
}
