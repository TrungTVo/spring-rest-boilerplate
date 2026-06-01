package com.example.boilerplate.commons.dtos;

import java.util.UUID;

public record CaretakerDTO(
        UUID id,
        String name,
        String shift,
        String specialty) {

}
