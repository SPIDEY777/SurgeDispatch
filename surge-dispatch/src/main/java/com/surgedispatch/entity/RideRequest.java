package com.surgedispatch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;



    @Entity
    @Getter
    public class RideRequest {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "rider_id", nullable = false)
        private Rider rider;
        @Setter
        @Column(nullable = false)
        private Double pickupLat;
        @Setter
        @Column(nullable = false)
        private Double pickupLng;
        @Setter
        @Column(nullable = false)
        private Double dropoffLat;
        @Setter
        @Column(nullable = false)
        private Double dropoffLng;
        @Setter
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private RideRequestStatus status = RideRequestStatus.REQUESTED;
        private LocalDateTime requestedAt;
        @Setter
        private LocalDateTime expiresAt;
        @PrePersist
        protected void onCreate() {
            this.requestedAt = LocalDateTime.now();
        }
        protected RideRequest() {}
        public RideRequest(Rider rider, Double pickupLat, Double pickupLng, Double dropoffLat, Double dropoffLng) {
            this.rider = rider;
            this.pickupLat = pickupLat;
            this.pickupLng = pickupLng;
            this.dropoffLat = dropoffLat;
            this.dropoffLng = dropoffLng;
        }
    }

