package com.caspercodes.aggregatorservice.repository;

import com.caspercodes.aggregatorservice.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, UUID> {
    Optional<Specialty> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}