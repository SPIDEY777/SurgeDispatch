package com.surgedispatch.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column( unique = true, nullable = false)
    private String email;

    private String phone;

    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
