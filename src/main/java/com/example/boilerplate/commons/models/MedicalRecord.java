package com.example.boilerplate.commons.models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    private String diagnosis;

    private String treatment;

    private String notes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", unique = true)
    @ToString.Exclude
    private Animal animal;

    public MedicalRecord(String diagnosis, String treatment, String notes) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
    }
}
