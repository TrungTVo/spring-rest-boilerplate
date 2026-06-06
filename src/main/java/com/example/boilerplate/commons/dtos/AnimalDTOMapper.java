package com.example.boilerplate.commons.dtos;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.boilerplate.commons.models.Animal;
import com.example.boilerplate.commons.models.Caretaker;
import com.example.boilerplate.commons.models.Habitat;
import com.example.boilerplate.commons.models.MedicalRecord;

@Service
public class AnimalDTOMapper implements Function<Animal, AnimalDTO> {

    @Override
    public AnimalDTO apply(Animal animal) {
        return new AnimalDTO(
                animal.getId(),
                animal.getName(),
                animal.getAge(),
                animal.getBalance(),
                this.toHabitatDTO(animal.getHabitat()),
                this.toMedicalRecordDTO(animal.getMedicalRecord()),
                animal.getCaretakers()
                        .stream()
                        .sorted(Comparator.comparing(Caretaker::getName, Comparator.nullsLast(String::compareTo)))
                        .map(this::toCaretakerDTO)
                        .collect(Collectors.toList())
                );
    }

    public HabitatDTO toHabitatDTO(Habitat habitat) {
        if (habitat == null) {
            return null;
        }
        return new HabitatDTO(habitat.getId(), habitat.getName(), habitat.getClimate(), habitat.getDescription());
    }

    public MedicalRecordDTO toMedicalRecordDTO(MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return null;
        }
        return new MedicalRecordDTO(
                medicalRecord.getId(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getTreatment(),
                medicalRecord.getNotes());
    }

    public CaretakerDTO toCaretakerDTO(Caretaker caretaker) {
        if (caretaker == null) {
            return null;
        }
        return new CaretakerDTO(caretaker.getId(), caretaker.getName(), caretaker.getShift(), caretaker.getSpecialty());
    }

}
