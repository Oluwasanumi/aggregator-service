package com.caspercodes.aggregatorservice.dto;

import jakarta.validation.constraints.NotNull;
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
public class UserInterestRequest {

    @NotNull(message = "Specialty IDs are required")
    private List<UUID> specialtyIds;

    private List<UUID> subSpecialtyIds;
}