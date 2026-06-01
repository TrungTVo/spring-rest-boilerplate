package com.example.boilerplate.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.boilerplate.commons.dtos.AnimalDTO;
import com.example.boilerplate.commons.dtos.AnimalRequest;

public interface AnimalInterface {
    void saveAnimals();

    List<AnimalDTO> getAllAnimals();

    Page<AnimalDTO> getFilteredAnimals(Pageable pageable);

    AnimalDTO createAnimal(AnimalRequest request);

    AnimalDTO getAnimal(UUID animalId);

    AnimalDTO updateAnimal(UUID animalId, AnimalRequest request);

    void deleteAnimal(UUID animalId);

    AnimalDTO assignHabitat(UUID animalId, UUID habitatId);

    AnimalDTO assignMedicalRecord(UUID animalId, UUID recordId);

    AnimalDTO addCaretaker(UUID animalId, UUID caretakerId);

    AnimalDTO removeCaretaker(UUID animalId, UUID caretakerId);
}
