package com.caspercodes.aggregatorservice.repository;

import com.caspercodes.aggregatorservice.model.Resource;
import com.caspercodes.aggregatorservice.model.ResourceType;
import com.caspercodes.aggregatorservice.model.SubSpecialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, UUID> {
    Page<Resource> findBySubSpecialty(SubSpecialty subSpecialty, Pageable pageable);
    Page<Resource> findBySubSpecialtyId(UUID subSpecialtyId, Pageable pageable);

    Page<Resource> findByResourceType(ResourceType resourceType, Pageable pageable);

    @Query("SELECT r FROM Resource r WHERE r.subSpecialty.specialty.id = :specialtyId")
    Page<Resource> findBySpecialtyId(UUID specialtyId, Pageable pageable);

    @Query("SELECT r FROM Resource r WHERE " +
            "r.subSpecialty.id IN (SELECT s.id FROM SubSpecialty s JOIN s.users u WHERE u.id = :userId) OR " +
            "r.subSpecialty.specialty.id IN (SELECT s.id FROM Specialty s JOIN s.users u WHERE u.id = :userId)")
    Page<Resource> findRecommendedResourcesForUser(UUID userId, Pageable pageable);

    // Search by title or description
    @Query("SELECT r FROM Resource r WHERE " +
            "LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Resource> searchResources(String query, Pageable pageable);

    // Get top rated resources
    @Query("SELECT r FROM Resource r WHERE r.rating IS NOT NULL ORDER BY r.rating DESC")
    List<Resource> findTopRatedResources(Pageable pageable);
}