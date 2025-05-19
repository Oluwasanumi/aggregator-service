package com.caspercodes.aggregatorservice.dto;

import com.caspercodes.aggregatorservice.model.ResourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "URL is required")
    private String url;

    @NotNull(message = "Resource type is required")
    private ResourceType resourceType;

    private String author;

    private String thumbnailUrl;

    private Double rating;

    private Integer durationMinutes;

    @NotNull(message = "Subspecialty ID is required")
    private UUID subSpecialtyId;
}