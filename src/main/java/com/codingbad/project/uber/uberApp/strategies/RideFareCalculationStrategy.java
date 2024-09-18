package com.codingbad.project.uber.uberApp.strategies;

import com.codingbad.project.uber.uberApp.entities.RideRequest;

public interface RideFareCalculationStrategy {

    double calculateFare(RideRequest rideRequest);

    Double RIDE_FARE_MULTIPLIER=10.0;
}
