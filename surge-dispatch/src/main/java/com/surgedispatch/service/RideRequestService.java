package com.surgedispatch.service;

import com.surgedispatch.dto.CreateRideRequest;
import com.surgedispatch.entity.Ride;
import com.surgedispatch.entity.RideRequest;
import com.surgedispatch.entity.RideRequestStatus;
import com.surgedispatch.entity.Rider;
import com.surgedispatch.exception.RideRequestNotFoundException;
import com.surgedispatch.exception.RiderNotFoundException;
import com.surgedispatch.repository.RideRepository;
import com.surgedispatch.repository.RideRequestRepository;
import com.surgedispatch.repository.RiderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RideRequestService {

    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final RideRepository rideRepository;
    private final MatchingEngineService matchingEngineService;

    public RideRequestService(RideRequestRepository rideRequestRepository,
                              RiderRepository riderRepository,
                              RideRepository rideRepository,
                              MatchingEngineService matchingEngineService) {
        this.rideRequestRepository = rideRequestRepository;
        this.riderRepository = riderRepository;
        this.rideRepository = rideRepository;
        this.matchingEngineService = matchingEngineService;
    }

    @Transactional
    public RideRequest createRideRequest(CreateRideRequest request) {
        Rider rider = riderRepository.findById(request.getRiderId())
                .orElseThrow(() -> new RiderNotFoundException("Rider not found with ID: " + request.getRiderId()));

        RideRequest rideRequest = new RideRequest(
                rider,
                request.getPickupLat(),
                request.getPickupLng(),
                request.getDropoffLat(),
                request.getDropoffLng()
        );

        // Auto-expire after 5 minutes if unmatched
        rideRequest.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        RideRequest savedRequest = rideRequestRepository.save(rideRequest);

        // Immediately trigger matching engine with cached candidates
        matchingEngineService.matchAndDispatch(savedRequest);

        return savedRequest;
    }

    @Transactional(readOnly = true)
    public RideRequest getRideRequestById(Long id) {
        return rideRequestRepository.findById(id)
                .orElseThrow(() -> new RideRequestNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Optional<Ride> getRideByRideRequestId(Long rideRequestId) {
        return rideRepository.findByRideRequestId(rideRequestId);
    }

    @Transactional(readOnly = true)
    public List<RideRequest> getAllRideRequests() {
        return rideRequestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<RideRequest> getRideRequestsByRiderId(Long riderId) {
        return rideRequestRepository.findByRiderId(riderId);
    }

    @Transactional
    public RideRequest cancelRideRequest(Long id) {
        RideRequest rideRequest = getRideRequestById(id);
        rideRequest.setStatus(RideRequestStatus.CANCELLED);
        return rideRequestRepository.save(rideRequest);
    }
}
