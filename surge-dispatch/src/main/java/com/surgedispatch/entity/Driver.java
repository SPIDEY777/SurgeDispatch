package com.surgedispatch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Setter
    private String name;

    @Setter
    @Column( unique = true, nullable = false)
    private String email;

    @Setter
    private String phone;
    @Column( unique = true, nullable = false)

    @Setter
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
