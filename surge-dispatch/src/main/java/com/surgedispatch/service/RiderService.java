package com.surgedispatch.service;

import com.surgedispatch.dto.CreateRiderRequest;
import com.surgedispatch.dto.UpdateRiderRequest;
import com.surgedispatch.entity.Rider;
import com.surgedispatch.exception.DuplicateEmailException;
import com.surgedispatch.exception.RiderNotFoundException;
import com.surgedispatch.repository.RiderRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    public Rider createRider(CreateRiderRequest createRiderRequest) {

        if (riderRepository.existsByEmail(createRiderRequest.getEmail())) {

            throw new DuplicateEmailException("A rider with this email already exists.");

        }

        Rider rider = new Rider(
                createRiderRequest.getName(),
                createRiderRequest.getEmail(),
                createRiderRequest.getPhone()
        );


        return riderRepository.save(rider);

    }

    public Rider getRiderById(Long id) {

        Optional<Rider> rider = riderRepository.findById(id);

        return rider.orElseThrow(
                () -> new RiderNotFoundException("Rider not found with id: " + id)
        );

    }

    public List<Rider> getAllRiders() {
        return riderRepository.findAll();
    }

    public Rider updateRider(Long id, UpdateRiderRequest request) {

        Optional<Rider> rider = riderRepository.findById(id);

        Rider existingRider = rider.orElseThrow(
                () -> new RiderNotFoundException("Rider not found with id:" + id)
        );
        if (request.getName() != null && !request.getName().isBlank()) {
            existingRider.setName(request.getName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {

            if (!existingRider.getEmail().equals(request.getEmail()) &&
                    riderRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateEmailException("A rider with this email already exists.");
            }
            existingRider.setEmail(request.getEmail());
        }

        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            existingRider.setPhone(request.getPhone());
        }


        return riderRepository.save(existingRider);

    }

    public void deleteRider(Long id) {
        Rider rider = riderRepository.findById(id)
                .orElseThrow(
                        () -> new RiderNotFoundException(
                                "Rider not found with id: " + id
                        )
                );

        riderRepository.delete(rider);

    }
}
