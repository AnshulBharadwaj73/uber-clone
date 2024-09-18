package com.codingbad.project.uber.uberApp.services;

import com.codingbad.project.uber.uberApp.entities.RideRequest;
import org.springframework.stereotype.Service;

@Service
public interface RideRequestService {

    RideRequest findRideByRequestById(Long rideRequestId);

    void update(RideRequest rideRequest);
}
