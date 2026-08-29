package com.surgedispatch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ride_request_id", nullable = false, unique = true)
    private RideRequest rideRequest;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;
    @Setter
    @Column(nullable = false)
    private Double basePrice;
    @Setter
    @Column(nullable = false)
    private Double surgeMultiplier = 1.0;
    @Setter
    @Column(nullable = false)
    private Double finalPrice;
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideRequestStatus status;
    @Setter
    private LocalDateTime confirmedAt;
    @Setter
    private LocalDateTime completedAt;

    protected Ride() {
    }

    public Ride(RideRequest rideRequest, Driver driver, Double basePrice, Double surgeMultiplier, Double finalPrice) {
        this.rideRequest = rideRequest;
        this.driver = driver;
        this.basePrice = basePrice;
        this.surgeMultiplier = surgeMultiplier;
        this.finalPrice = finalPrice;
        this.status = RideRequestStatus.MATCHED;

    }
}
