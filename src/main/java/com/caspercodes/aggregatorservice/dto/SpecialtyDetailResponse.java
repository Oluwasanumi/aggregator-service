package com.caspercodes.aggregatorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialtyDetailResponse {
    private UUID id;
    private String name;
    private String description;
    private List<SubSpecialtyResponse> subSpecialties;
}