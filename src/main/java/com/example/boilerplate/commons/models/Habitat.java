package com.example.boilerplate.commons.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Habitat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;

    private String climate;

    private String description;

    @OneToMany(mappedBy = "habitat")
    @ToString.Exclude
    private Set<Animal> animals = new HashSet<>();

    public Habitat(String name, String climate, String description) {
        this.name = name;
        this.climate = climate;
        this.description = description;
    }

    public void addAnimal(Animal animal) {
        this.animals.add(animal);
        animal.setHabitat(this);
    }

    public void removeAnimal(Animal animal) {
        this.animals.remove(animal);
        animal.setHabitat(null);
    }
}
