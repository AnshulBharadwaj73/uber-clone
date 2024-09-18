package com.codingbad.project.uber.uberApp.strategies.impl;

import com.codingbad.project.uber.uberApp.entities.Driver;
import com.codingbad.project.uber.uberApp.entities.RideRequest;
import com.codingbad.project.uber.uberApp.repositories.DriverRepository;
import com.codingbad.project.uber.uberApp.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;
    @Override
    public List<Driver> findMatchingDrivers(RideRequest rideRequest) {
        return driverRepository.findTenNearestDrivers(rideRequest.getPickupLocation());
    }
}
