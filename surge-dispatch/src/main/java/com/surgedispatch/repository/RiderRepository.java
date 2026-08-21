package com.surgedispatch.repository;

import com.surgedispatch.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider,Long> {

    boolean existsByEmail(String email);


}
