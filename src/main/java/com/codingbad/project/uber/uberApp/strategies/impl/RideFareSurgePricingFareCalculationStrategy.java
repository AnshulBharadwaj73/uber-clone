package com.codingbad.project.uber.uberApp.strategies.impl;

import com.codingbad.project.uber.uberApp.entities.RideRequest;
import com.codingbad.project.uber.uberApp.services.DistanceService;
import com.codingbad.project.uber.uberApp.strategies.RideFareCalculationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {


    private final DistanceService distanceService;

    private static final double SURGE_FACTOR = 2;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        Double distance =distanceService.calculateDistance(rideRequest.getPickupLocation(),rideRequest.getDropOffLocation());

        return distance * SURGE_FACTOR * RIDE_FARE_MULTIPLIER;
    }
}
