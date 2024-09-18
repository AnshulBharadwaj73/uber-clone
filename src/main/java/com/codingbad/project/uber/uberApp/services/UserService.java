package com.codingbad.project.uber.uberApp.services;

import com.codingbad.project.uber.uberApp.entities.User;
import com.codingbad.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.codingbad.project.uber.uberApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public final class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not Found "+ id));
    }
}
