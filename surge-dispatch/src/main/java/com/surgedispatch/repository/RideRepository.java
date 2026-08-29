package com.surgedispatch.repository;

import com.surgedispatch.entity.Ride;
import com.surgedispatch.entity.RideRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    Optional<Ride> findByRideRequestId(Long rideRequestId);

    List<Ride> findByDriverId(Long driverId);

    List<Ride> findByStatus(RideRequestStatus status);
}
