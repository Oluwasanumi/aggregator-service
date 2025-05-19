package com.caspercodes.aggregatorservice.service;

import com.caspercodes.aggregatorservice.dto.LoginRequest;
import com.caspercodes.aggregatorservice.dto.TokenResponse;
import com.caspercodes.aggregatorservice.dto.UserProfileResponse;
import com.caspercodes.aggregatorservice.dto.UserRegistrationRequest;
import com.caspercodes.aggregatorservice.model.User;
import com.caspercodes.aggregatorservice.repository.UserRepository;
import com.caspercodes.aggregatorservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(UserRegistrationRequest request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }


        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();


        userRepository.save(user);


        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        return TokenResponse.builder()
                .token(token)
                .isNewUser(true)
                .build();
    }

    public TokenResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase(),
                        request.getPassword()
                )
        );


        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));


        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(userDetails);
        return TokenResponse.builder()
                .token(token)
                .isNewUser(false)
                .build();
    }

    public UserProfileResponse getUserProfile(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}