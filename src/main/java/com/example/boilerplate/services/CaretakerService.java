package com.example.boilerplate.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.boilerplate.commons.dtos.AnimalDTOMapper;
import com.example.boilerplate.commons.dtos.CaretakerDTO;
import com.example.boilerplate.commons.dtos.CaretakerRequest;
import com.example.boilerplate.commons.models.Animal;
import com.example.boilerplate.commons.models.Caretaker;
import com.example.boilerplate.exceptions.NoRecordFoundException;
import com.example.boilerplate.repositories.CaretakerRepository;

@Service
public class CaretakerService {

    @Autowired
    private CaretakerRepository caretakerRepository;

    @Autowired
    private AnimalDTOMapper animalDTOMapper;

    @Transactional(rollbackFor = Exception.class)
    public CaretakerDTO createCaretaker(CaretakerRequest request) {
        Caretaker caretaker = new Caretaker(request.name(), request.shift(), request.specialty());
        return this.animalDTOMapper.toCaretakerDTO(this.caretakerRepository.save(caretaker));
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<CaretakerDTO> getAllCaretakers() {
        return this.caretakerRepository.findAll()
                .stream()
                .map(this.animalDTOMapper::toCaretakerDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public CaretakerDTO getCaretaker(UUID caretakerId) {
        return this.animalDTOMapper.toCaretakerDTO(this.findCaretaker(caretakerId));
    }

    @Transactional(rollbackFor = Exception.class)
    public CaretakerDTO updateCaretaker(UUID caretakerId, CaretakerRequest request) {
        Caretaker caretaker = this.findCaretaker(caretakerId);
        caretaker.setName(request.name());
        caretaker.setShift(request.shift());
        caretaker.setSpecialty(request.specialty());
        return this.animalDTOMapper.toCaretakerDTO(this.caretakerRepository.save(caretaker));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCaretaker(UUID caretakerId) {
        Caretaker caretaker = this.findCaretaker(caretakerId);
        List<Animal> animals = new ArrayList<>(caretaker.getAnimals());
        animals.forEach(animal -> animal.removeCaretaker(caretaker));
        this.caretakerRepository.delete(caretaker);
    }

    private Caretaker findCaretaker(UUID caretakerId) {
        return this.caretakerRepository.findById(caretakerId)
                .orElseThrow(() -> new NoRecordFoundException("No caretaker found with id " + caretakerId));
    }
}
