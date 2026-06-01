package com.example.boilerplate.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boilerplate.commons.models.Habitat;

public interface HabitatRepository extends JpaRepository<Habitat, UUID> {
}
