package com.surgedispatch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Rider {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Setter
    private String name;

    @Column( unique = true, nullable = false)
    @Setter
    private String email;

    @Setter
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
