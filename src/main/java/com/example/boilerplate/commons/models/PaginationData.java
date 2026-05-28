package com.example.boilerplate.commons.models;

public record PaginationData<T>(
        T result,
        String lastCursor) {
}
