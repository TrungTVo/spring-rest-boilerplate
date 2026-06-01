package com.example.boilerplate.commons.dtos;

import java.util.UUID;

public record HabitatDTO(
        UUID id,
        String name,
        String climate,
        String description) {

}
