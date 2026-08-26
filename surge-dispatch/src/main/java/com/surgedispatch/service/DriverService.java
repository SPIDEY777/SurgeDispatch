package com.surgedispatch.service;

import com.surgedispatch.dto.CreateDriverRequest;
import com.surgedispatch.dto.DriverResponse;
import com.surgedispatch.entity.Driver;
import com.surgedispatch.entity.Rider;
import com.surgedispatch.exception.DriverNotFoundException;
import com.surgedispatch.exception.DuplicateEmailException;
import com.surgedispatch.exception.DuplicateLicenseNumberException;
import com.surgedispatch.exception.RiderNotFoundException;
import com.surgedispatch.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

  private  final   DriverRepository driverRepository;

   public DriverService(DriverRepository driverRepository){this.driverRepository=driverRepository;}

    public Driver createDriver(CreateDriverRequest createDriverRequest){

       if(driverRepository.existsByLicenseNumber(createDriverRequest.getLicenseNumber())){
           throw new DuplicateLicenseNumberException("A driver with this license number already exists.");
        }
       if(driverRepository.existsByEmail(createDriverRequest.getEmail())){
           throw new DuplicateEmailException("a driver with this email already exists");
       }

       Driver driver = new Driver(
               createDriverRequest.getName(),
               createDriverRequest.getEmail(),
               createDriverRequest.getPhone(),
               createDriverRequest.getLicenseNumber()
       );
       return driverRepository.save(driver);
    }

public Driver getDriverById(Long id){


    Optional<Driver> driver = driverRepository.findById(id);


    return driver.orElseThrow(
            () -> new DriverNotFoundException("Driver not found with id: " + id)
    );
}

public List<Driver> getAllDrivers(){

       return driverRepository.findAll();
}


  }

