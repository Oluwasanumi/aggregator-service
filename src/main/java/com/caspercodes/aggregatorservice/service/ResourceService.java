package com.caspercodes.aggregatorservice.service;

import com.caspercodes.aggregatorservice.dto.PagedResponse;
import com.caspercodes.aggregatorservice.dto.ResourceFilterRequest;
import com.caspercodes.aggregatorservice.dto.ResourceRequest;
import com.caspercodes.aggregatorservice.dto.ResourceResponse;
import com.caspercodes.aggregatorservice.model.Resource;
import com.caspercodes.aggregatorservice.model.SubSpecialty;
import com.caspercodes.aggregatorservice.model.User;
import com.caspercodes.aggregatorservice.repository.ResourceRepository;
import com.caspercodes.aggregatorservice.repository.SubSpecialtyRepository;
import com.caspercodes.aggregatorservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final SubSpecialtyRepository subSpecialtyRepository;
    private final UserRepository userRepository;

    public PagedResponse<ResourceResponse> getResourcesByFilter(ResourceFilterRequest filter) {
        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(Sort.Direction.DESC, "rating", "createdAt")
        );

        Page<Resource> resources;


        if (filter.getSubSpecialtyId() != null) {
            resources = resourceRepository.findBySubSpecialtyId(filter.getSubSpecialtyId(), pageable);
        } else if (filter.getSpecialtyId() != null) {
            resources = resourceRepository.findBySpecialtyId(filter.getSpecialtyId(), pageable);
        } else if (filter.getResourceType() != null) {
            resources = resourceRepository.findByResourceType(filter.getResourceType(), pageable);
        } else if (filter.getSearchQuery() != null && !filter.getSearchQuery().trim().isEmpty()) {
            resources = resourceRepository.searchResources(filter.getSearchQuery().trim(), pageable);
        } else {
            resources = resourceRepository.findAll(pageable);
        }

        List<ResourceResponse> content = resources.getContent().stream()
                .map(this::mapToResourceResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                resources.getNumber(),
                resources.getSize(),
                resources.getTotalElements(),
                resources.getTotalPages(),
                resources.isLast()
        );
    }

    public PagedResponse<ResourceResponse> getRecommendedResources(String email, int page, int size) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "rating", "createdAt")
        );

        Page<Resource> resources = resourceRepository.findRecommendedResourcesForUser(user.getId(), pageable);

        List<ResourceResponse> content = resources.getContent().stream()
                .map(this::mapToResourceResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                resources.getNumber(),
                resources.getSize(),
                resources.getTotalElements(),
                resources.getTotalPages(),
                resources.isLast()
        );
    }

    public ResourceResponse getResourceById(UUID resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        return mapToResourceResponse(resource);
    }

    @Transactional
    public ResourceResponse createResource(ResourceRequest request) {
        SubSpecialty subSpecialty = subSpecialtyRepository.findById(request.getSubSpecialtyId())
                .orElseThrow(() -> new IllegalArgumentException("SubSpecialty not found"));

        Resource resource = Resource.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .url(request.getUrl())
                .resourceType(request.getResourceType())
                .author(request.getAuthor())
                .thumbnailUrl(request.getThumbnailUrl())
                .rating(request.getRating())
                .durationMinutes(request.getDurationMinutes())
                .subSpecialty(subSpecialty)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Resource savedResource = resourceRepository.save(resource);
        return mapToResourceResponse(savedResource);
    }

    @Transactional
    public ResourceResponse updateResource(UUID resourceId, ResourceRequest request) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        SubSpecialty subSpecialty = subSpecialtyRepository.findById(request.getSubSpecialtyId())
                .orElseThrow(() -> new IllegalArgumentException("SubSpecialty not found"));

        resource.setTitle(request.getTitle());
        resource.setDescription(request.getDescription());
        resource.setUrl(request.getUrl());
        resource.setResourceType(request.getResourceType());
        resource.setAuthor(request.getAuthor());
        resource.setThumbnailUrl(request.getThumbnailUrl());
        resource.setRating(request.getRating());
        resource.setDurationMinutes(request.getDurationMinutes());
        resource.setSubSpecialty(subSpecialty);
        resource.setUpdatedAt(LocalDateTime.now());

        Resource updatedResource = resourceRepository.save(resource);
        return mapToResourceResponse(updatedResource);
    }

    @Transactional
    public void deleteResource(UUID resourceId) {
        if (!resourceRepository.existsById(resourceId)) {
            throw new IllegalArgumentException("Resource not found");
        }

        resourceRepository.deleteById(resourceId);
    }

    private ResourceResponse mapToResourceResponse(Resource resource) {
        return ResourceResponse.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .description(resource.getDescription())
                .url(resource.getUrl())
                .resourceType(resource.getResourceType())
                .author(resource.getAuthor())
                .thumbnailUrl(resource.getThumbnailUrl())
                .rating(resource.getRating())
                .durationMinutes(resource.getDurationMinutes())
                .createdAt(resource.getCreatedAt())
                .subSpecialtyId(resource.getSubSpecialty().getId())
                .subSpecialtyName(resource.getSubSpecialty().getName())
                .specialtyId(resource.getSubSpecialty().getSpecialty().getId())
                .specialtyName(resource.getSubSpecialty().getSpecialty().getName())
                .build();
    }
}