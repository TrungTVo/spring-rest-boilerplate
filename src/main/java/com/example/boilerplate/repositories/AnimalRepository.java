package com.example.boilerplate.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boilerplate.commons.models.Animal;

public interface AnimalRepository extends JpaRepository<Animal, UUID> {
    List<Animal> findByName(String name, Pageable pageable);
}
