package com.caspercodes.aggregatorservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "subspecialties")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubSpecialty {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;


    @ManyToMany(mappedBy = "subSpecialties")
    private Set<User> users = new HashSet<>();


    @OneToMany(mappedBy = "subSpecialty", cascade = CascadeType.ALL)
    private Set<Resource> resources = new HashSet<>();
}