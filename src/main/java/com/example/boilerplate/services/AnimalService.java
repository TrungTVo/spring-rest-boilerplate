package com.example.boilerplate.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.boilerplate.commons.dtos.AnimalDTO;
import com.example.boilerplate.commons.dtos.AnimalDTOMapper;
import com.example.boilerplate.commons.dtos.AnimalRequest;
import com.example.boilerplate.commons.models.Animal;
import com.example.boilerplate.commons.models.Caretaker;
import com.example.boilerplate.commons.models.Habitat;
import com.example.boilerplate.commons.models.MedicalRecord;
import com.example.boilerplate.exceptions.NoRecordFoundException;
import com.example.boilerplate.interfaces.AnimalInterface;
import com.example.boilerplate.repositories.AnimalRepository;
import com.example.boilerplate.repositories.CaretakerRepository;
import com.example.boilerplate.repositories.HabitatRepository;
import com.example.boilerplate.repositories.MedicalRecordRepository;

@Service
public class AnimalService implements AnimalInterface {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private HabitatRepository habitatRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private CaretakerRepository caretakerRepository;

    @Autowired
    private AnimalDTOMapper animalDTOMapper;

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<AnimalDTO> getAllAnimals() {
        return this.animalRepository.findAll()
                .stream()
                .map(this.animalDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Page<AnimalDTO> getFilteredAnimals(Pageable pageable) {
        Page<Animal> pAnimal = this.animalRepository.findAll(pageable);
        List<Animal> lAnimal = pAnimal.getContent();
        List<AnimalDTO> lAnimalDTO = lAnimal.stream()
                .map(this.animalDTOMapper)
                .collect(Collectors.toList());
        return new PageImpl<AnimalDTO>(lAnimalDTO, pageable, pAnimal.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAnimals() {
        List<Animal> animals = List.of(
                new Animal("dog", 1, "pass1"),
                new Animal("cat", 2, "pass2"),
                new Animal("tiger", 3, "pass3"),
                new Animal("lion", 4, "pass4"),
                new Animal("bird", 5, "pass5"),
                new Animal("fish", 6, "pass6"));
        this.animalRepository.saveAll(animals);
    }

    @Override
    @Retryable(
        retryFor    = RuntimeException.class,
        maxAttempts = 3,
        backoff     = @Backoff(delay = 1000, multiplier = 2)
    )
    public AnimalDTO createAnimal(AnimalRequest request) {
        return executeCreateAnimal(request);
    }

    @Recover
    public AnimalDTO cannotCreateAnimal(RuntimeException ex, AnimalRequest request) {
        throw new RuntimeException("Failed to create animal after 3 retries: " + ex.getMessage());
    }


    @Transactional(rollbackFor = Exception.class)
    public AnimalDTO executeCreateAnimal(AnimalRequest request) {
        Animal animal = new Animal(request.name(), request.age(), request.password());
        return this.animalDTOMapper.apply(this.animalRepository.save(animal));
    }


    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public AnimalDTO getAnimal(UUID animalId) {
        return this.animalDTOMapper.apply(this.findAnimal(animalId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AnimalDTO updateAnimal(UUID animalId, AnimalRequest request) {
        Animal animal = this.findAnimal(animalId);
        animal.setName(request.name());
        animal.setAge(request.age());
        animal.setPassword(request.password());
        return this.animalDTOMapper.apply(this.animalRepository.save(animal));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAnimal(UUID animalId) {
        Animal animal = this.findAnimal(animalId);
        if (animal.getMedicalRecord() != null) {
            animal.getMedicalRecord().setAnimal(null);
            animal.setMedicalRecord(null);
        }
        if (animal.getHabitat() != null) {
            animal.getHabitat().getAnimals().remove(animal);
            animal.setHabitat(null);
        }
        animal.getCaretakers()
                .forEach(caretaker -> caretaker.getAnimals().remove(animal));
        animal.getCaretakers().clear();
        this.animalRepository.delete(animal);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AnimalDTO assignHabitat(UUID animalId, UUID habitatId) {
        Animal animal = this.findAnimal(animalId);
        Habitat habitat = this.habitatRepository.findById(habitatId)
                .orElseThrow(() -> new NoRecordFoundException("No habitat found with id " + habitatId));

        if (animal.getHabitat() != null) {
            animal.getHabitat().getAnimals().remove(animal);
        }
        animal.setHabitat(habitat);
        habitat.getAnimals().add(animal);
        return this.animalDTOMapper.apply(this.animalRepository.save(animal));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AnimalDTO assignMedicalRecord(UUID animalId, UUID recordId) {
        Animal animal = this.findAnimal(animalId);
        MedicalRecord medicalRecord = this.medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new NoRecordFoundException("No medical record found with id " + recordId));

        if (animal.getMedicalRecord() != null) {
            animal.getMedicalRecord().setAnimal(null);
        }
        if (medicalRecord.getAnimal() != null) {
            medicalRecord.getAnimal().setMedicalRecord(null);
        }
        animal.setMedicalRecord(medicalRecord);
        medicalRecord.setAnimal(animal);
        this.medicalRecordRepository.save(medicalRecord);
        return this.animalDTOMapper.apply(this.animalRepository.save(animal));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AnimalDTO addCaretaker(UUID animalId, UUID caretakerId) {
        Animal animal = this.findAnimal(animalId);
        Caretaker caretaker = this.caretakerRepository.findById(caretakerId)
                .orElseThrow(() -> new NoRecordFoundException("No caretaker found with id " + caretakerId));

        animal.addCaretaker(caretaker);
        return this.animalDTOMapper.apply(this.animalRepository.save(animal));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AnimalDTO removeCaretaker(UUID animalId, UUID caretakerId) {
        Animal animal = this.findAnimal(animalId);
        Caretaker caretaker = this.caretakerRepository.findById(caretakerId)
                .orElseThrow(() -> new NoRecordFoundException("No caretaker found with id " + caretakerId));

        animal.removeCaretaker(caretaker);
        return this.animalDTOMapper.apply(this.animalRepository.save(animal));
    }

    private Animal findAnimal(UUID animalId) {
        return this.animalRepository.findById(animalId)
                .orElseThrow(() -> new NoRecordFoundException("No animal found with id " + animalId));
    }

}
