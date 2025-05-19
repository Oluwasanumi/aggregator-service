package com.caspercodes.aggregatorservice.controller;

import com.caspercodes.aggregatorservice.dto.SpecialtyResponse;
import com.caspercodes.aggregatorservice.dto.SubSpecialtyResponse;
import com.caspercodes.aggregatorservice.dto.UserInterestRequest;
import com.caspercodes.aggregatorservice.security.CurrentUser;
import com.caspercodes.aggregatorservice.service.UserInterestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class UserInterestController {

    private final UserInterestService userInterestService;
    private final CurrentUser currentUser;

    @PostMapping
    public ResponseEntity<Void> setUserInterests(@Valid @RequestBody UserInterestRequest request) {
        userInterestService.setUserInterests(currentUser.getEmail(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/specialties")
    public ResponseEntity<List<SpecialtyResponse>> getUserSpecialties() {
        List<SpecialtyResponse> specialties = userInterestService.getUserSpecialties(currentUser.getEmail());
        return ResponseEntity.ok(specialties);
    }

    @GetMapping("/subspecialties")
    public ResponseEntity<List<SubSpecialtyResponse>> getUserSubSpecialties() {
        List<SubSpecialtyResponse> subspecialties = userInterestService.getUserSubSpecialties(currentUser.getEmail());
        return ResponseEntity.ok(subspecialties);
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> hasSelectedInterests() {
        boolean hasInterests = userInterestService.hasSelectedInterests(currentUser.getEmail());
        return ResponseEntity.ok(hasInterests);
    }
}