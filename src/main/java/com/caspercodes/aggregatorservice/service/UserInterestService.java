package com.caspercodes.aggregatorservice.service;

import com.caspercodes.aggregatorservice.dto.SpecialtyResponse;
import com.caspercodes.aggregatorservice.dto.SubSpecialtyResponse;
import com.caspercodes.aggregatorservice.dto.UserInterestRequest;
import com.caspercodes.aggregatorservice.model.Specialty;
import com.caspercodes.aggregatorservice.model.SubSpecialty;
import com.caspercodes.aggregatorservice.model.User;
import com.caspercodes.aggregatorservice.repository.SpecialtyRepository;
import com.caspercodes.aggregatorservice.repository.SubSpecialtyRepository;
import com.caspercodes.aggregatorservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInterestService {

    private final UserRepository userRepository;
    private final SpecialtyRepository specialtyRepository;
    private final SubSpecialtyRepository subSpecialtyRepository;
    private final SpecialtyService specialtyService;

    @Transactional
    public void setUserInterests(String email, UserInterestRequest request) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        Set<Specialty> specialties = new HashSet<>();
        if (request.getSpecialtyIds() != null && !request.getSpecialtyIds().isEmpty()) {
            for (UUID specialtyId : request.getSpecialtyIds()) {
                Specialty specialty = specialtyRepository.findById(specialtyId)
                        .orElseThrow(() -> new IllegalArgumentException("Specialty not found: " + specialtyId));
                specialties.add(specialty);
            }
        }


        Set<SubSpecialty> subSpecialties = new HashSet<>();
        if (request.getSubSpecialtyIds() != null && !request.getSubSpecialtyIds().isEmpty()) {
            for (UUID subSpecialtyId : request.getSubSpecialtyIds()) {
                SubSpecialty subSpecialty = subSpecialtyRepository.findById(subSpecialtyId)
                        .orElseThrow(() -> new IllegalArgumentException("SubSpecialty not found: " + subSpecialtyId));
                subSpecialties.add(subSpecialty);

                // Add the parent specialty automatically if not already included
                if (!specialties.contains(subSpecialty.getSpecialty())) {
                    specialties.add(subSpecialty.getSpecialty());
                }
            }
        }


        user.setSpecialties(specialties);
        user.setSubSpecialties(subSpecialties);

        userRepository.save(user);
    }

    public List<SpecialtyResponse> getUserSpecialties(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user.getSpecialties().stream()
                .map(specialty -> SpecialtyResponse.builder()
                        .id(specialty.getId())
                        .name(specialty.getName())
                        .description(specialty.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    public List<SubSpecialtyResponse> getUserSubSpecialties(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user.getSubSpecialties().stream()
                .map(subSpecialty -> SubSpecialtyResponse.builder()
                        .id(subSpecialty.getId())
                        .name(subSpecialty.getName())
                        .description(subSpecialty.getDescription())
                        .specialtyId(subSpecialty.getSpecialty().getId())
                        .specialtyName(subSpecialty.getSpecialty().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public boolean hasSelectedInterests(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return !user.getSpecialties().isEmpty();
    }
}