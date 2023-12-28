package com.example.boilerplate.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.boilerplate.commons.dtos.AnimalDTO;

public interface AnimalInterface {
    void saveAnimals();

    List<AnimalDTO> getAllAnimals();

    Page<AnimalDTO> getFilteredAnimals(Pageable pageable);
}
