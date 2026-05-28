package com.example.boilerplate.commons.dtos;

import java.time.Instant;
import java.util.UUID;

/**
 * Animal DTO to exclude {@code password} field in the HTTP Response
 */
public record AnimalDTO(
        UUID id,
        Instant createdAt,
        String name,
        int age) {

}
