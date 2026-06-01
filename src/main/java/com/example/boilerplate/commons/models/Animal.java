package com.example.boilerplate.commons.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    private String name;

    private int age;

    private String password;

    @OneToOne(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitat_id")
    @ToString.Exclude
    private Habitat habitat;

    @ManyToMany
    @JoinTable(name = "animal_caretakers", joinColumns = @JoinColumn(name = "animal_id"), inverseJoinColumns = @JoinColumn(name = "caretaker_id"))
    @ToString.Exclude
    private Set<Caretaker> caretakers = new HashSet<>();

    public Animal(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    public void addCaretaker(Caretaker caretaker) {
        this.caretakers.add(caretaker);
        caretaker.getAnimals().add(this);
    }

    public void removeCaretaker(Caretaker caretaker) {
        this.caretakers.remove(caretaker);
        caretaker.getAnimals().remove(this);
    }
}
