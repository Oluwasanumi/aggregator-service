package com.caspercodes.aggregatorservice.dto;

import com.caspercodes.aggregatorservice.model.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceFilterRequest {
    private UUID specialtyId;
    private UUID subSpecialtyId;
    private ResourceType resourceType;
    private String searchQuery;
    private Integer minRating;
    private Integer maxDurationMinutes;

    // For pagination
    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;
}