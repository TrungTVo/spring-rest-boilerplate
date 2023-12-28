package com.example.boilerplate.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.boilerplate.commons.dtos.AnimalDTO;
import com.example.boilerplate.commons.dtos.AnimalDTOMapper;
import com.example.boilerplate.commons.models.Animal;
import com.example.boilerplate.interfaces.AnimalInterface;
import com.example.boilerplate.repositories.AnimalRepository;

@Service
public class AnimalService implements AnimalInterface {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalDTOMapper animalDTOMapper;

    @Override
    public List<AnimalDTO> getAllAnimals() {
        return this.animalRepository.findAll()
                .stream()
                .map(this.animalDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AnimalDTO> getFilteredAnimals(Pageable pageable) {
        Page<Animal> pAnimal = this.animalRepository.findAll(pageable);
        List<Animal> lAnimal = pAnimal.getContent();
        List<AnimalDTO> lAnimalDTO = lAnimal.stream()
                .map(this.animalDTOMapper)
                .collect(Collectors.toList());
        return new PageImpl<AnimalDTO>(lAnimalDTO, pageable, lAnimalDTO.size());
    }

    @Override
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

}
