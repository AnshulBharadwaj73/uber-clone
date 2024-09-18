package com.codingbad.project.uber.uberApp.strategies;

import com.codingbad.project.uber.uberApp.entities.Driver;
import com.codingbad.project.uber.uberApp.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDrivers(RideRequest rideRequest);
}
