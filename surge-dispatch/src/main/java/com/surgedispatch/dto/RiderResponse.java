package com.surgedispatch.dto;

import com.surgedispatch.entity.Rider;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RiderResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createdAt;


    public RiderResponse(Rider rider) {
        this.id = rider.getId();
        this.name = rider.getName();
        this.email = rider.getEmail();
        this.phone = rider.getPhone();
        this.createdAt = rider.getCreatedAt();
    }
}


