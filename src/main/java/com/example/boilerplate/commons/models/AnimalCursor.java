package com.example.boilerplate.commons.models;

import java.util.UUID;

public record AnimalCursor(
        String field,
        String direction,
        String value,
        String createdAt,
        UUID id) {
}
