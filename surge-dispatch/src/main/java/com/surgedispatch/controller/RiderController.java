package com.surgedispatch.controller;


import com.surgedispatch.dto.CreateRiderRequest;
import com.surgedispatch.entity.Rider;
import com.surgedispatch.service.RiderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
public class RiderController {

     private final RiderService riderService;

     public RiderController(RiderService riderService){
         this.riderService = riderService;
     }

     @PostMapping("/api/v1/riders")
     public ResponseEntity<Rider> createRider(@Valid @RequestBody CreateRiderRequest createRiderRequest) {

         Rider createdRider = riderService.createRider(createRiderRequest);
         return ResponseEntity.status(HttpStatus.CREATED).body(createdRider);
     }



}
