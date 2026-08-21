package com.surgedispatch.controller;


import com.surgedispatch.dto.CreateRiderRequest;
import com.surgedispatch.dto.RiderResponse;
import com.surgedispatch.entity.Rider;
import com.surgedispatch.service.RiderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
public class RiderController {

     private final RiderService riderService;

     public RiderController(RiderService riderService){
         this.riderService = riderService;
     }

     @PostMapping("/api/v1/riders")
     public ResponseEntity<RiderResponse> createRider(
             @Valid @RequestBody CreateRiderRequest createRiderRequest) {

         Rider createdRider = riderService.createRider(createRiderRequest);

         RiderResponse response = new RiderResponse(createdRider);

         return ResponseEntity
                 .status(HttpStatus.CREATED)
                 .body(response);
     }

     @GetMapping("/api/v1/riders/{id}")
    public ResponseEntity<RiderResponse> getRider(@PathVariable Long id){



         Rider rider = riderService.getRiderById(id);
         RiderResponse response = new RiderResponse(rider);

          return ResponseEntity.ok(response);



    }



}
