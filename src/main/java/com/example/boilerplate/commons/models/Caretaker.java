package com.example.boilerplate.commons.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Caretaker {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;

    private String shift;

    private String specialty;

    @ManyToMany(mappedBy = "caretakers")
    @ToString.Exclude
    private Set<Animal> animals = new HashSet<>();

    public Caretaker(String name, String shift, String specialty) {
        this.name = name;
        this.shift = shift;
        this.specialty = specialty;
    }
}
