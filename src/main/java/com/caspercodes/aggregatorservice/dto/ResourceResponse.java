package com.caspercodes.aggregatorservice.dto;

import com.caspercodes.aggregatorservice.model.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceResponse {
    private UUID id;
    private String title;
    private String description;
    private String url;
    private ResourceType resourceType;
    private String author;
    private String thumbnailUrl;
    private Double rating;
    private Integer durationMinutes;
    private LocalDateTime createdAt;
    private UUID subSpecialtyId;
    private String subSpecialtyName;
    private UUID specialtyId;
    private String specialtyName;
}