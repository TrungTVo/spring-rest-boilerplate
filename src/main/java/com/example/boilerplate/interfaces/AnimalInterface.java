package com.example.boilerplate.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.boilerplate.commons.models.Animal;

public interface AnimalInterface {
    void saveAnimals();

    List<Animal> getAllAnimals();

    Page<Animal> getFilteredAnimals(Pageable pageable);
}
