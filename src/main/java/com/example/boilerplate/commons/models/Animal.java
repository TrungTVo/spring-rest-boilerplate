package com.example.boilerplate.commons.models;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = {
        @Index(name = "idx_animal_name_cursor", columnList = "name, created_at, id"),
        @Index(name = "idx_animal_age_cursor", columnList = "age, created_at, id"),
        @Index(name = "idx_animal_created_at_cursor", columnList = "created_at, id")
})
@Data
@NoArgsConstructor
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    private String name;

    private int age;

    private String password;

    public Animal(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    public Animal(String name, int age, String password, Instant createdAt) {
        this.name = name;
        this.age = age;
        this.password = password;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }
}
