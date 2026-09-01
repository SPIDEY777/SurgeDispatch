package com.surgedispatch.service;

import com.surgedispatch.dto.CreateRiderRequest;
import com.surgedispatch.dto.UpdateRiderRequest;
import com.surgedispatch.entity.Rider;
import com.surgedispatch.exception.DuplicateEmailException;
import com.surgedispatch.exception.RiderNotFoundException;
import com.surgedispatch.repository.RiderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public Rider getRiderById(Long id) {
        return riderRepository.findById(id)
                .orElseThrow(() -> new RiderNotFoundException("Rider not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Rider> getAllRiders() {
        return riderRepository.findAll();
    }

    @Transactional
    public Rider updateRider(Long id, UpdateRiderRequest request) {
        Rider existingRider = getRiderById(id);

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

    @Transactional
    public void deleteRider(Long id) {
        Rider rider = getRiderById(id);
        riderRepository.delete(rider);
    }
}
