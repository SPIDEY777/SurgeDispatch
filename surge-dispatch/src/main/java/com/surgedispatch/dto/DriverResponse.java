package com.surgedispatch.dto;

import com.surgedispatch.entity.Driver;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DriverResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String licenseNumber;
    private LocalDateTime createdAt;

    public DriverResponse(Driver driver) {
        this.id = driver.getId();
        this.name = driver.getName();
        this.email = driver.getEmail();
        this.phone = driver.getPhone();
        this.licenseNumber = driver.getLicenseNumber();
        this.createdAt = driver.getCreatedAt();
    }
}