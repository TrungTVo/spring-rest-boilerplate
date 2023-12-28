package com.example.boilerplate.commons.dtos;

/**
 * Animal DTO to exclude {@code password} field in the HTTP Response
 */
public record AnimalDTO(
        Integer id,
        String name,
        int age) {

}
