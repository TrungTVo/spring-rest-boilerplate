package com.example.boilerplate.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.boilerplate.commons.dtos.AnimalDTO;
import com.example.boilerplate.commons.models.PaginationData;

public interface AnimalInterface {
    void saveAnimals();

    List<AnimalDTO> getAllAnimals();

    // offset pagination with page, size, sort
    PaginationData<Page<AnimalDTO>> getFilteredAnimals(Pageable pageable);

    // cursor pagination with size, sort, cursor
    PaginationData<List<AnimalDTO>> getFilteredAnimalsByCursor(int size, Sort sort, String cursor);
}
