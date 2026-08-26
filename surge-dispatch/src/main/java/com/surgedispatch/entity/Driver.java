package com.surgedispatch.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column( unique = true, nullable = false)
    private String email;

    private String phone;
    @Column( unique = true, nullable = false)
    private String licenseNumber;

    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    protected Driver() {
    }

    public Driver(String name, String email, String phone,String licenseNumber) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
    }

}
