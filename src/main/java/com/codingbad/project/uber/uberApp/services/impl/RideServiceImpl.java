package com.codingbad.project.uber.uberApp.services.impl;

import com.codingbad.project.uber.uberApp.entities.Driver;
import com.codingbad.project.uber.uberApp.entities.Ride;
import com.codingbad.project.uber.uberApp.entities.RideRequest;
import com.codingbad.project.uber.uberApp.entities.Rider;
import com.codingbad.project.uber.uberApp.entities.enums.RideRequestStatus;
import com.codingbad.project.uber.uberApp.entities.enums.RideStatus;
import com.codingbad.project.uber.uberApp.repositories.RideRepository;
import com.codingbad.project.uber.uberApp.services.RideRequestService;
import com.codingbad.project.uber.uberApp.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId).orElseThrow(() -> new RuntimeException("Ride with id not found "+rideId));
    }

    @Override
    public Ride crateNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);
        Ride ride= modelMapper.map(rideRequest,Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        ride.setOtp(generateRandomOTP());

        rideRequestService.update(rideRequest);
        return rideRepository.save(ride);

    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }


    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider,pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver,pageRequest);
    }

    private String generateRandomOTP(){
        Random random =new Random();
        int otpInt = random.nextInt(10000);
        return String.format("%4d",otpInt);
    }
}
