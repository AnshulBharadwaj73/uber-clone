package com.codingbad.project.uber.uberApp.services.impl;

import com.codingbad.project.uber.uberApp.dto.DriverDto;
import com.codingbad.project.uber.uberApp.dto.RideDto;
import com.codingbad.project.uber.uberApp.dto.RideRequestDto;
import com.codingbad.project.uber.uberApp.dto.RiderDto;
import com.codingbad.project.uber.uberApp.entities.Driver;
import com.codingbad.project.uber.uberApp.entities.Ride;
import com.codingbad.project.uber.uberApp.entities.RideRequest;
import com.codingbad.project.uber.uberApp.entities.User;
import com.codingbad.project.uber.uberApp.entities.enums.RideRequestStatus;
import com.codingbad.project.uber.uberApp.entities.enums.RideStatus;
import com.codingbad.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.codingbad.project.uber.uberApp.repositories.DriverRepository;
import com.codingbad.project.uber.uberApp.services.*;
import com.fasterxml.jackson.core.PrettyPrinter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;


    @Override
    public RideDto acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideByRequestById(rideRequestId);

        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)){
            throw new RuntimeException("Ride request cannot be accepted,  status is "+rideRequest.getRideRequestStatus());
        }
        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getAvailable()){
            throw new RuntimeException("Driver not available ");
        }

        Driver savedDriver = updateDriverAvailability(currentDriver,false);

        Ride ride = rideService.crateNewRide(rideRequest,savedDriver);
        return modelMapper.map(ride,RideDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Ride ride =rideService.getRideById(rideId);

        Driver driver=getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride cannot be cancelled, invalid status: "+ride.getRideStatus());
        }

        rideService.updateRideStatus(ride,RideStatus.CANCELLED);
        updateDriverAvailability(driver,false);
        return modelMapper.map(ride,RideDto.class);
    }

    @Override
    public RideDto startRide(Long rideId, String otp) {

        Ride ride=rideService.getRideById(rideId);
        Driver driver=getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride status is not confirmed hence cannot be started, status: "+ride.getRideStatus());
        }

        if(!otp.equals(ride.getOtp())){
            throw new RuntimeException("Otp is not valid "+otp);
        }


        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide= rideService.updateRideStatus(ride,RideStatus.ONGOING);
        paymentService.createNewPayment(savedRide);
        ratingService.createNewRating(savedRide);

        return modelMapper.map(savedRide,RideDto.class);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
        Ride ride=rideService.getRideById(rideId);
        Driver driver=getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("Ride status is not ONGOING hence cannot be ended, status: "+ride.getRideStatus());
        }
        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide=rideService.updateRideStatus(ride,RideStatus.ENDED);
        updateDriverAvailability(driver,true);

        paymentService.processPayment(ride);

        return modelMapper.map(savedRide, RideDto.class);


    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride=rideService.getRideById(rideId);
        Driver driver=getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot rate a ride as he has not accepted it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride status is not ended hence cannot be rated, status: "+ride.getRideStatus());
        }

        return ratingService.rateRider(ride,rating);
    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver,DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver driver=getCurrentDriver();
        return rideService.getAllRidesOfDriver(getCurrentDriver(), pageRequest).map(
                ride -> modelMapper.map(ride,RideDto.class)
        );
    }

    @Override
    public Driver getCurrentDriver() {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return driverRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("Driver not associated with user with "+user.getId()));
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }
}
