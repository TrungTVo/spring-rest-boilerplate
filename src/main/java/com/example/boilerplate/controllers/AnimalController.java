package com.example.boilerplate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boilerplate.commons.models.Response;
import com.example.boilerplate.commons.models.ResponseMetadata;
import com.example.boilerplate.services.AnimalService;

@RestController
@RequestMapping("animal")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AnimalController {
    private Logger logger = LoggerFactory.getLogger(AnimalController.class);

    @Autowired
    private AnimalService animalService;

    @PostMapping("save")
    public ResponseEntity<Response<?>> saveAnimals() {
        logger.info("Create and save animals...");
        this.animalService.saveAnimals();
        Response<?> messageRes = Response.builder()
                .data("Saved animals")
                .metadata(ResponseMetadata.success("Successfully returned all animals", HttpStatus.CREATED.value()))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(messageRes);
    }

    @GetMapping("all")
    public ResponseEntity<Response<?>> getAllAnimals() {
        logger.info("Get all animals...");
        Response<?> messageRes = Response.builder()
                .data(this.animalService.getAllAnimals())
                .metadata(ResponseMetadata.success("Successfully returned all animals", HttpStatus.OK.value()))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageRes);
    }

    /**
     * Filter animals with page, size or sort.
     * Sample endpoint:
     * {@code /animal/filter?page=1&size=3&sort=name,desc }
     * 
     * @param pageable includes `page`, `size`, `sort`
     * @return
     */
    @GetMapping("filter")
    public ResponseEntity<Response<?>> getFilteredAnimals(@ParameterObject Pageable pageable) {
        logger.info("Filter animals... " + pageable.toString());

        Response<?> messageRes = Response.builder()
                .data(this.animalService.getFilteredAnimals(pageable))
                .metadata(ResponseMetadata.success("Successfully returned filtered animals", HttpStatus.OK.value()))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(messageRes);
    }
}
