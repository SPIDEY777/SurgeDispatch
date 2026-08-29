package com.surgedispatch.repository;

import com.surgedispatch.entity.RideRequest;
import com.surgedispatch.entity.RideRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {

    List<RideRequest> findByRiderId(Long riderId);

    List<RideRequest> findByStatus(RideRequestStatus status);

    List<RideRequest> findByStatusAndExpiresAtBefore(RideRequestStatus status, LocalDateTime now);
}
