package com.surgedispatch.service;

import com.surgedispatch.dto.CreateRiderRequest;
import com.surgedispatch.entity.Rider;
import com.surgedispatch.repository.RiderRepository;
import org.springframework.stereotype.Service;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

   public Rider createRider(CreateRiderRequest createRiderRequest){

        if (riderRepository.existsByEmail(createRiderRequest.getEmail())) {

            throw new IllegalArgumentException("A rider with this email already exists.");

        }

        Rider rider = new Rider(
                createRiderRequest.getName(),
                createRiderRequest.getEmail(),
                createRiderRequest.getPhone()
        );


        return riderRepository.save(rider);

    }

}
