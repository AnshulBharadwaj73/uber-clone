package com.codingbad.project.uber.uberApp.services;

import com.codingbad.project.uber.uberApp.dto.DriverDto;
import com.codingbad.project.uber.uberApp.dto.SignupDto;
import com.codingbad.project.uber.uberApp.dto.UserDto;

public interface AuthService {

    String[] login(String email,String password);

    UserDto signup(SignupDto signupDto);

    DriverDto onboardNewDriver(Long userId, String vehicleId);

    String refreshToken(java.lang.String refreshToken);
}
