package com.caspercodes.aggregatorservice.controller;

import com.caspercodes.aggregatorservice.dto.PagedResponse;
import com.caspercodes.aggregatorservice.dto.ResourceFilterRequest;
import com.caspercodes.aggregatorservice.dto.ResourceRequest;
import com.caspercodes.aggregatorservice.dto.ResourceResponse;
import com.caspercodes.aggregatorservice.model.ResourceType;
import com.caspercodes.aggregatorservice.security.CurrentUser;
import com.caspercodes.aggregatorservice.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;
    private final CurrentUser currentUser;

    @GetMapping
    public ResponseEntity<PagedResponse<ResourceResponse>> getResources(
            @RequestParam(required = false) UUID specialtyId,
            @RequestParam(required = false) UUID subSpecialtyId,
            @RequestParam(required = false) ResourceType resourceType,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ResourceFilterRequest filter = ResourceFilterRequest.builder()
                .specialtyId(specialtyId)
                .subSpecialtyId(subSpecialtyId)
                .resourceType(resourceType)
                .searchQuery(query)
                .page(page)
                .size(size)
                .build();

        PagedResponse<ResourceResponse> resources = resourceService.getResourcesByFilter(filter);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/recommended")
    public ResponseEntity<PagedResponse<ResourceResponse>> getRecommendedResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<ResourceResponse> resources = resourceService.getRecommendedResources(
                currentUser.getEmail(), page, size);

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<ResourceResponse> getResourceById(@PathVariable UUID resourceId) {
        ResourceResponse resource = resourceService.getResourceById(resourceId);
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity<ResourceResponse> createResource(@Valid @RequestBody ResourceRequest request) {
        ResourceResponse resource = resourceService.createResource(request);
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{resourceId}")
    public ResponseEntity<ResourceResponse> updateResource(
            @PathVariable UUID resourceId,
            @Valid @RequestBody ResourceRequest request) {

        ResourceResponse resource = resourceService.updateResource(resourceId, request);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Void> deleteResource(@PathVariable UUID resourceId) {
        resourceService.deleteResource(resourceId);
        return ResponseEntity.noContent().build();
    }
}