package com.example.boilerplate.commons.dtos;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.example.boilerplate.commons.models.Animal;

@Service
public class AnimalDTOMapper implements Function<Animal, AnimalDTO> {

    @Override
    public AnimalDTO apply(Animal animal) {
        return new AnimalDTO(animal.getId(), animal.getName(), animal.getAge());
    }

}
