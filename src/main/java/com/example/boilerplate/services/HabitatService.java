package com.example.boilerplate.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.boilerplate.commons.dtos.AnimalDTOMapper;
import com.example.boilerplate.commons.dtos.HabitatDTO;
import com.example.boilerplate.commons.dtos.HabitatRequest;
import com.example.boilerplate.commons.models.Animal;
import com.example.boilerplate.commons.models.Habitat;
import com.example.boilerplate.exceptions.NoRecordFoundException;
import com.example.boilerplate.repositories.HabitatRepository;

@Service
public class HabitatService {

    @Autowired
    private HabitatRepository habitatRepository;

    @Autowired
    private AnimalDTOMapper animalDTOMapper;

    @Transactional(rollbackFor = Exception.class)
    public HabitatDTO createHabitat(HabitatRequest request) {
        Habitat habitat = new Habitat(request.name(), request.climate(), request.description());
        return this.animalDTOMapper.toHabitatDTO(this.habitatRepository.save(habitat));
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<HabitatDTO> getAllHabitats() {
        return this.habitatRepository.findAll()
                .stream()
                .map(this.animalDTOMapper::toHabitatDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public HabitatDTO getHabitat(UUID habitatId) {
        return this.animalDTOMapper.toHabitatDTO(this.findHabitat(habitatId));
    }

    @Transactional(rollbackFor = Exception.class)
    public HabitatDTO updateHabitat(UUID habitatId, HabitatRequest request) {
        Habitat habitat = this.findHabitat(habitatId);
        habitat.setName(request.name());
        habitat.setClimate(request.climate());
        habitat.setDescription(request.description());
        return this.animalDTOMapper.toHabitatDTO(this.habitatRepository.save(habitat));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteHabitat(UUID habitatId) {
        Habitat habitat = this.findHabitat(habitatId);
        List<Animal> animals = new ArrayList<>(habitat.getAnimals());
        animals.forEach(habitat::removeAnimal);
        this.habitatRepository.delete(habitat);
    }

    private Habitat findHabitat(UUID habitatId) {
        return this.habitatRepository.findById(habitatId)
                .orElseThrow(() -> new NoRecordFoundException("No habitat found with id " + habitatId));
    }
}
