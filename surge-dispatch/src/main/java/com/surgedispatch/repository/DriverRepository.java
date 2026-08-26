package com.surgedispatch.repository;

import com.surgedispatch.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver,Long> {

    boolean existsByEmail(String email);

    boolean existsByLicenseNumber(String licenseNumber);

}
