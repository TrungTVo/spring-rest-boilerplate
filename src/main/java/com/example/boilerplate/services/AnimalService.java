package com.example.boilerplate.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.boilerplate.commons.dtos.AnimalDTO;
import com.example.boilerplate.commons.dtos.AnimalDTOMapper;
import com.example.boilerplate.commons.models.Animal;
import com.example.boilerplate.commons.models.AnimalCursor;
import com.example.boilerplate.commons.models.PaginationData;
import com.example.boilerplate.interfaces.AnimalInterface;
import com.example.boilerplate.repositories.AnimalRepository;

@Service
public class AnimalService implements AnimalInterface {
    private static final String[] RANDOM_ANIMAL_NAMES = {
            "dog", "cat", "tiger", "lion", "bird", "fish", "bear", "horse", "rabbit", "fox"
    };

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalDTOMapper animalDTOMapper;

    @Autowired
    private AnimalCursorService animalCursorService;

    /**
     * Get all animals without pagination - not recommended for large datasets
     */
    @Override
    public List<AnimalDTO> getAllAnimals() {
        return this.animalRepository.findAll()
                .stream()
                .map(this.animalDTOMapper)
                .collect(Collectors.toList());
    }

    /**
     * Offset pagination with page, size, sort
     */
    @Override
    public PaginationData<Page<AnimalDTO>> getFilteredAnimals(Pageable pageable) {
        Sort.Order order = this.animalCursorService.validateSort(pageable.getSort());
        Pageable stablePageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                this.animalCursorService.withCursorTieBreakers(order));

        Page<Animal> pAnimal = this.animalRepository.findAll(stablePageable);
        Page<AnimalDTO> pAnimalDTO = pAnimal.map(this.animalDTOMapper);
        String lastCursor = pAnimal.getContent().isEmpty()
                ? ""
                : this.animalCursorService.encode(pAnimal.getContent().get(pAnimal.getContent().size() - 1), order);

        return new PaginationData<>(pAnimalDTO, lastCursor);
    }


    /**
     * Cursor pagination with size, sort, cursor
     */
    @Override
    public PaginationData<List<AnimalDTO>> getFilteredAnimalsByCursor(int size, Sort sort, String cursor) {
        if (size <= 0) {
            throw new IllegalArgumentException("Cursor page size must be greater than zero");
        }

        Sort.Order order = this.animalCursorService.validateSort(sort);
        AnimalCursor decodedCursor = this.animalCursorService.decode(cursor, order);
        List<Animal> animals = decodedCursor == null
                ? this.findFirstCursorPage(size, order)
                : this.findNextCursorPage(size, order, decodedCursor);
        List<AnimalDTO> animalDTOs = animals.stream()
                .map(this.animalDTOMapper)
                .collect(Collectors.toList());
        String lastCursor = animals.isEmpty()
                ? ""
                : this.animalCursorService.encode(animals.get(animals.size() - 1), order);

        return new PaginationData<>(animalDTOs, lastCursor);
    }


    /**
     * Helper methods for cursor pagination - find first page without cursor (if we cannot decode cursor string)
     */
    private List<Animal> findFirstCursorPage(int size, Sort.Order order) {
        Pageable pageable = PageRequest.of(0, size, this.animalCursorService.withCursorTieBreakers(order));
        return this.animalRepository.findAll(pageable).getContent();
    }


    /**
     * Helper methods for cursor pagination - find next page with cursor
     */
    private List<Animal> findNextCursorPage(int size, Sort.Order order, AnimalCursor cursor) {
        boolean ascending = order.isAscending();

        return switch (order.getProperty()) {
            case "createdAt" -> this.findNextCreatedAtPage(size, ascending, cursor);
            case "name" -> this.findNextNamePage(size, ascending, cursor);
            case "age" -> this.findNextAgePage(size, ascending, cursor);
            default -> throw new IllegalArgumentException("Unsupported cursor sort field: " + order.getProperty());
        };
    }


    private List<Animal> findNextCreatedAtPage(int size, boolean ascending, AnimalCursor cursor) {
        Pageable limit = PageRequest.of(0, size);
        List<Animal> animals = new ArrayList<>(ascending
                ? this.animalRepository.findNextByCreatedAtAsc(
                        this.animalCursorService.parseCreatedAt(cursor.createdAt()), cursor.id(), limit)
                : this.animalRepository.findNextByCreatedAtDesc(
                        this.animalCursorService.parseCreatedAt(cursor.createdAt()), cursor.id(), limit));

        return animals;
    }


    private List<Animal> findNextNamePage(int size, boolean ascending, AnimalCursor cursor) {
        Pageable limit = PageRequest.of(0, size);
        List<Animal> animals = new ArrayList<>(ascending
                ? this.animalRepository.findNextByNameAsc(
                        cursor.value(), this.animalCursorService.parseCreatedAt(cursor.createdAt()), cursor.id(), limit)
                : this.animalRepository.findNextByNameDesc(
                        cursor.value(), this.animalCursorService.parseCreatedAt(cursor.createdAt()), cursor.id(), limit));

        return animals;
    }


    private List<Animal> findNextAgePage(int size, boolean ascending, AnimalCursor cursor) {
        Integer age = parseCursorInteger(cursor.value());
        Pageable limit = PageRequest.of(0, size);
        List<Animal> animals = new ArrayList<>(ascending
                ? this.animalRepository.findNextByAgeAsc(
                        age, this.animalCursorService.parseCreatedAt(cursor.createdAt()), cursor.id(), limit)
                : this.animalRepository.findNextByAgeDesc(
                        age, this.animalCursorService.parseCreatedAt(cursor.createdAt()), cursor.id(), limit));

        return animals;
    }


    private Integer parseCursorInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid cursor", ex);
        }
    }


    /**
     * Generate and save 1000 random animals for testing purposes.
     */
    @Override
    public void saveAnimals() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        List<Animal> animals = new ArrayList<>(100);

        for (int i = 0; i < 1000; i++) {
            String name = RANDOM_ANIMAL_NAMES[random.nextInt(RANDOM_ANIMAL_NAMES.length)];
            int age = random.nextInt(1, 31);
            String password = "pass-" + random.nextInt(100_000, 1_000_000);

            animals.add(new Animal(name, age, password));
        }

        this.animalRepository.saveAll(animals);
    }

}
