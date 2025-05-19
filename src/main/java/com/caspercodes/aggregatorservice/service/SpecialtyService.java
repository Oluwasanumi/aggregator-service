package com.caspercodes.aggregatorservice.service;

import com.caspercodes.aggregatorservice.dto.SpecialtyDetailResponse;
import com.caspercodes.aggregatorservice.dto.SpecialtyResponse;
import com.caspercodes.aggregatorservice.dto.SubSpecialtyRequest;
import com.caspercodes.aggregatorservice.dto.SubSpecialtyResponse;
import com.caspercodes.aggregatorservice.model.Specialty;
import com.caspercodes.aggregatorservice.model.SubSpecialty;
import com.caspercodes.aggregatorservice.repository.SpecialtyRepository;
import com.caspercodes.aggregatorservice.repository.SubSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SubSpecialtyRepository subSpecialtyRepository;

    public List<SpecialtyResponse> getAllSpecialties() {
        return specialtyRepository.findAll().stream()
                .map(this::mapToSpecialtyResponse)
                .collect(Collectors.toList());
    }

    public SpecialtyDetailResponse getSpecialtyById(UUID specialtyId) {
        Specialty specialty = specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new IllegalArgumentException("Specialty not found"));

        List<SubSpecialtyResponse> subSpecialties = subSpecialtyRepository.findBySpecialty(specialty).stream()
                .map(this::mapToSubSpecialtyResponse)
                .collect(Collectors.toList());

        return SpecialtyDetailResponse.builder()
                .id(specialty.getId())
                .name(specialty.getName())
                .description(specialty.getDescription())
                .subSpecialties(subSpecialties)
                .build();
    }

    public List<SubSpecialtyResponse> getSubSpecialtiesBySpecialtyId(UUID specialtyId) {
        return subSpecialtyRepository.findBySpecialtyId(specialtyId).stream()
                .map(this::mapToSubSpecialtyResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubSpecialtyResponse createSubSpecialty(SubSpecialtyRequest request) {
        Specialty specialty = specialtyRepository.findById(request.getSpecialtyId())
                .orElseThrow(() -> new IllegalArgumentException("Specialty not found"));

        // Check if subspecialty with same name already exists for this specialty
        if (subSpecialtyRepository.findByNameIgnoreCaseAndSpecialtyId(request.getName(), request.getSpecialtyId()).isPresent()) {
            throw new IllegalArgumentException("Subspecialty with this name already exists for the given specialty");
        }

        SubSpecialty subSpecialty = SubSpecialty.builder()
                .name(request.getName())
                .description(request.getDescription())
                .specialty(specialty)
                .build();

        SubSpecialty savedSubSpecialty = subSpecialtyRepository.save(subSpecialty);
        return mapToSubSpecialtyResponse(savedSubSpecialty);
    }

    private SpecialtyResponse mapToSpecialtyResponse(Specialty specialty) {
        return SpecialtyResponse.builder()
                .id(specialty.getId())
                .name(specialty.getName())
                .description(specialty.getDescription())
                .build();
    }

    private SubSpecialtyResponse mapToSubSpecialtyResponse(SubSpecialty subSpecialty) {
        return SubSpecialtyResponse.builder()
                .id(subSpecialty.getId())
                .name(subSpecialty.getName())
                .description(subSpecialty.getDescription())
                .specialtyId(subSpecialty.getSpecialty().getId())
                .specialtyName(subSpecialty.getSpecialty().getName())
                .build();
    }
}