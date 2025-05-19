package com.caspercodes.aggregatorservice.controller;

import com.caspercodes.aggregatorservice.dto.LoginRequest;
import com.caspercodes.aggregatorservice.dto.TokenResponse;
import com.caspercodes.aggregatorservice.dto.UserProfileResponse;
import com.caspercodes.aggregatorservice.dto.UserRegistrationRequest;
import com.caspercodes.aggregatorservice.security.CurrentUser;
import com.caspercodes.aggregatorservice.service.AuthService;
import com.caspercodes.aggregatorservice.service.UserInterestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserInterestService userInterestService;
    private final CurrentUser currentUser;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        TokenResponse tokenResponse = authService.register(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);

        boolean hasInterests = userInterestService.hasSelectedInterests(request.getEmail());
        tokenResponse.setIsNewUser(!hasInterests);

        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile() {
        UserProfileResponse profile = authService.getUserProfile(currentUser.getEmail());
        return ResponseEntity.ok(profile);
    }
}