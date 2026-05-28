package com.example.boilerplate.services;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.boilerplate.commons.models.Animal;
import com.example.boilerplate.commons.models.AnimalCursor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AnimalCursorService {
    private static final Set<String> SUPPORTED_SORT_FIELDS = Set.of("createdAt", "name", "age");

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Sort.Order validateSort(Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return Sort.Order.asc("createdAt");
        }

        if (sort.stream().count() != 1) {
            throw new IllegalArgumentException("Cursor pagination supports exactly one sort field");
        }

        Sort.Order order = sort.iterator().next();
        if (!SUPPORTED_SORT_FIELDS.contains(order.getProperty())) {
            throw new IllegalArgumentException("Unsupported cursor sort field: " + order.getProperty());
        }

        return order;
    }

    public Sort withCursorTieBreakers(Sort.Order order) {
        if ("createdAt".equals(order.getProperty())) {
            return Sort.by(order, new Sort.Order(order.getDirection(), "id"));
        }
        return Sort.by(
                order,
                new Sort.Order(order.getDirection(), "createdAt"),
                new Sort.Order(order.getDirection(), "id"));
    }

    public String encode(Animal animal, Sort.Order order) {
        if (animal == null) {
            return "";
        }

        AnimalCursor cursor = new AnimalCursor(
                order.getProperty(),
                order.getDirection().name().toLowerCase(),
                cursorValue(animal, order.getProperty()),
                animal.getCreatedAt().toString(),
                animal.getId());
        try {
            byte[] json = objectMapper.writeValueAsBytes(cursor);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(json);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unable to encode cursor", ex);
        }
    }

    public AnimalCursor decode(String encodedCursor, Sort.Order order) {
        if (encodedCursor == null || encodedCursor.isBlank()) {
            return null;
        }

        try {
            byte[] decoded = Base64.getUrlDecoder().decode(encodedCursor);
            AnimalCursor cursor = objectMapper.readValue(new String(decoded, StandardCharsets.UTF_8), AnimalCursor.class);
            validateCursorMatchesSort(cursor, order);
            return cursor;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid cursor", ex);
        }
    }

    private void validateCursorMatchesSort(AnimalCursor cursor, Sort.Order order) {
        if (cursor == null || cursor.field() == null || cursor.direction() == null || cursor.value() == null
                || cursor.createdAt() == null || cursor.id() == null) {
            throw new IllegalArgumentException("Invalid cursor");
        }

        if (!cursor.field().equals(order.getProperty())
                || !cursor.direction().equals(order.getDirection().name().toLowerCase())) {
            throw new IllegalArgumentException("Cursor does not match sort");
        }
    }

    private String cursorValue(Animal animal, String field) {
        return switch (field) {
            case "id" -> animal.getId().toString();
            case "createdAt" -> animal.getCreatedAt().toString();
            case "name" -> animal.getName();
            case "age" -> Integer.toString(animal.getAge());
            default -> throw new IllegalArgumentException("Unsupported cursor sort field: " + field);
        };
    }

    public Instant parseCreatedAt(String createdAt) {
        try {
            return Instant.parse(createdAt);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid cursor", ex);
        }
    }
}
