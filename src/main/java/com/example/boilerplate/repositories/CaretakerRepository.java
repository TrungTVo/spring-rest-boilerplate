package com.example.boilerplate.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boilerplate.commons.models.Caretaker;

public interface CaretakerRepository extends JpaRepository<Caretaker, UUID> {
}
