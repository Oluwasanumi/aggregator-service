package com.caspercodes.aggregatorservice.controller;

import com.caspercodes.aggregatorservice.dto.SpecialtyDetailResponse;
import com.caspercodes.aggregatorservice.dto.SpecialtyResponse;
import com.caspercodes.aggregatorservice.dto.SubSpecialtyRequest;
import com.caspercodes.aggregatorservice.dto.SubSpecialtyResponse;
import com.caspercodes.aggregatorservice.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @GetMapping("/public")
    public ResponseEntity<List<SpecialtyResponse>> getAllSpecialties() {
        List<SpecialtyResponse> specialties = specialtyService.getAllSpecialties();
        return ResponseEntity.ok(specialties);
    }

    @GetMapping("/{specialtyId}")
    public ResponseEntity<SpecialtyDetailResponse> getSpecialtyById(@PathVariable UUID specialtyId) {
        SpecialtyDetailResponse specialty = specialtyService.getSpecialtyById(specialtyId);
        return ResponseEntity.ok(specialty);
    }

    @GetMapping("/{specialtyId}/subspecialties")
    public ResponseEntity<List<SubSpecialtyResponse>> getSubSpecialtiesBySpecialtyId(@PathVariable UUID specialtyId) {
        List<SubSpecialtyResponse> subspecialties = specialtyService.getSubSpecialtiesBySpecialtyId(specialtyId);
        return ResponseEntity.ok(subspecialties);
    }

    @PostMapping("/subspecialties")
    public ResponseEntity<SubSpecialtyResponse> createSubSpecialty(@Valid @RequestBody SubSpecialtyRequest request) {
        SubSpecialtyResponse subspecialty = specialtyService.createSubSpecialty(request);
        return ResponseEntity.ok(subspecialty);
    }
}