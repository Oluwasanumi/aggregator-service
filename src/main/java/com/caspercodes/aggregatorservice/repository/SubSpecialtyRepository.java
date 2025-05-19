package com.caspercodes.aggregatorservice.repository;

import com.caspercodes.aggregatorservice.model.Specialty;
import com.caspercodes.aggregatorservice.model.SubSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubSpecialtyRepository extends JpaRepository<SubSpecialty, UUID> {
    List<SubSpecialty> findBySpecialty(Specialty specialty);
    List<SubSpecialty> findBySpecialtyId(UUID specialtyId);
    Optional<SubSpecialty> findByNameIgnoreCaseAndSpecialtyId(String name, UUID specialtyId);
}