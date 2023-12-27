package com.example.boilerplate.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.boilerplate.commons.models.Animal;
import com.example.boilerplate.interfaces.AnimalInterface;
import com.example.boilerplate.repositories.AnimalRepository;

@Service
public class AnimalService implements AnimalInterface {

    @Autowired
    private AnimalRepository animalRepository;

    @Override
    public List<Animal> getAllAnimals() {
        return this.animalRepository.findAll();
    }

    @Override
    public Page<Animal> getFilteredAnimals(Pageable pageable) {
        return this.animalRepository.findAll(pageable);
    }

    @Override
    public void saveAnimals() {
        List<Animal> animals = List.of(
                new Animal("dog", 1),
                new Animal("cat", 2),
                new Animal("tiger", 3),
                new Animal("lion", 4),
                new Animal("bird", 5),
                new Animal("fish", 6));
        this.animalRepository.saveAll(animals);
    }

}
