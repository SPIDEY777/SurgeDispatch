package com.surgedispatch.service;

import com.surgedispatch.dto.CreateRiderRequest;
import com.surgedispatch.entity.Rider;
import com.surgedispatch.exception.DuplicateEmailException;
import com.surgedispatch.exception.RiderNotFoundException;
import com.surgedispatch.repository.RiderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

   public Rider createRider(CreateRiderRequest createRiderRequest){

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
    public Rider getRiderById(Long id){

        Optional<Rider> rider = riderRepository.findById(id);

        return rider.orElseThrow(
                () -> new RiderNotFoundException("Rider not found with id: " + id)
        );

    }

}
