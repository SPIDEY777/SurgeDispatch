package com.surgedispatch.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
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

    protected Rider() {
    }


    public Rider(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

}
