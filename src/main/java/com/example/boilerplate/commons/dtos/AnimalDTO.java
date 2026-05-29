package com.example.boilerplate.commons.dtos;

import java.util.UUID;

/**
 * Animal DTO to exclude {@code password} field in the HTTP Response
 */
public record AnimalDTO(
        UUID id,
        String name,
        int age) {

}
