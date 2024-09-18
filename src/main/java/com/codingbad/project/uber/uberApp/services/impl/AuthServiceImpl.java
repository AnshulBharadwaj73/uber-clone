package com.codingbad.project.uber.uberApp.services.impl;

import com.codingbad.project.uber.uberApp.configs.SecurityConfig;
import com.codingbad.project.uber.uberApp.dto.DriverDto;
import com.codingbad.project.uber.uberApp.dto.SignupDto;
import com.codingbad.project.uber.uberApp.dto.UserDto;
import com.codingbad.project.uber.uberApp.entities.Driver;
import com.codingbad.project.uber.uberApp.entities.Rider;
import com.codingbad.project.uber.uberApp.entities.User;
import com.codingbad.project.uber.uberApp.entities.enums.Role;
import com.codingbad.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.codingbad.project.uber.uberApp.exceptions.RuntimeConflictException;
import com.codingbad.project.uber.uberApp.repositories.UserRepository;
import com.codingbad.project.uber.uberApp.security.JWTService;
import com.codingbad.project.uber.uberApp.services.AuthService;
import com.codingbad.project.uber.uberApp.services.DriverService;
import com.codingbad.project.uber.uberApp.services.RiderService;
import com.codingbad.project.uber.uberApp.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.codingbad.project.uber.uberApp.entities.enums.Role.DRIVER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;


    @Override
    public String[] login(String email, String password) {

        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email,password)
        );

        User user=(User)authentication.getPrincipal();

        String accessToken= jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new String[]{accessToken,refreshToken};
    }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {

        User userEmail =userRepository.findByEmail(signupDto.getEmail()).orElse(null);
        if(userEmail!=null){
            throw new RuntimeConflictException("cannot signup ,User already exists with email "+signupDto.getEmail());
        }


        User user = modelMapper.map(signupDto,User.class);
        user.setRoles(Set.of(Role.RIDER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        riderService.createNewRider(savedUser);
        // TODO add wallet related service here

        walletService.createNewWallet(savedUser);

        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId,String vehicleId) {
        User user=userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id "+ userId));

        if(user.getRoles().contains(DRIVER)) throw new RuntimeConflictException("User with id "+userId+" is already driver");

        Driver createDriver = Driver.builder()
                .user(user)
                .rating(0.0)
                .available(true)
                .vehicleId(vehicleId)
                .build();
        user.getRoles().add(DRIVER);
        userRepository.save(user);
        Driver savedDriver = driverService.createNewDriver(createDriver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found " +
                "with id: "+userId));

        return jwtService.generateAccessToken(user);
    }
}
