package com.example.boilerplate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import com.example.boilerplate.commons.dtos.AnimalRequest;
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

    @PostMapping("create")
    public ResponseEntity<Response<?>> createAnimal(@RequestBody AnimalRequest request) {
        logger.info("Create animal...");
        return this.response(
                this.animalService.createAnimal(request),
                "Successfully created animal",
                HttpStatus.CREATED);
    }

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

    @GetMapping("{animalId}")
    public ResponseEntity<Response<?>> getAnimal(@PathVariable UUID animalId) {
        logger.info("Get animal... " + animalId);
        return this.response(
                this.animalService.getAnimal(animalId),
                "Successfully returned animal",
                HttpStatus.OK);
    }

    @PutMapping("{animalId}")
    public ResponseEntity<Response<?>> updateAnimal(
            @PathVariable UUID animalId,
            @RequestBody AnimalRequest request) {
        logger.info("Update animal... " + animalId);
        return this.response(
                this.animalService.updateAnimal(animalId, request),
                "Successfully updated animal",
                HttpStatus.OK);
    }

    @DeleteMapping("{animalId}")
    public ResponseEntity<Response<?>> deleteAnimal(@PathVariable UUID animalId) {
        logger.info("Delete animal... " + animalId);
        this.animalService.deleteAnimal(animalId);
        return this.response(
                "Deleted animal",
                "Successfully deleted animal",
                HttpStatus.OK);
    }

    @PutMapping("{animalId}/habitat/{habitatId}")
    public ResponseEntity<Response<?>> assignHabitat(
            @PathVariable UUID animalId,
            @PathVariable UUID habitatId) {
        logger.info("Assign habitat to animal... " + animalId);
        return this.response(
                this.animalService.assignHabitat(animalId, habitatId),
                "Successfully assigned habitat to animal",
                HttpStatus.OK);
    }

    @PutMapping("{animalId}/medical-record/{recordId}")
    public ResponseEntity<Response<?>> assignMedicalRecord(
            @PathVariable UUID animalId,
            @PathVariable UUID recordId) {
        logger.info("Assign medical record to animal... " + animalId);
        return this.response(
                this.animalService.assignMedicalRecord(animalId, recordId),
                "Successfully assigned medical record to animal",
                HttpStatus.OK);
    }

    @PutMapping("{animalId}/caretakers/{caretakerId}")
    public ResponseEntity<Response<?>> addCaretaker(
            @PathVariable UUID animalId,
            @PathVariable UUID caretakerId) {
        logger.info("Add caretaker to animal... " + animalId);
        return this.response(
                this.animalService.addCaretaker(animalId, caretakerId),
                "Successfully added caretaker to animal",
                HttpStatus.OK);
    }

    @DeleteMapping("{animalId}/caretakers/{caretakerId}")
    public ResponseEntity<Response<?>> removeCaretaker(
            @PathVariable UUID animalId,
            @PathVariable UUID caretakerId) {
        logger.info("Remove caretaker from animal... " + animalId);
        return this.response(
                this.animalService.removeCaretaker(animalId, caretakerId),
                "Successfully removed caretaker from animal",
                HttpStatus.OK);
    }

    /**
     * Filter animals with page, size or sort.
     * this is OFFSET pagination pattern
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

    private ResponseEntity<Response<?>> response(Object data, String message, HttpStatus status) {
        Response<?> messageRes = Response.builder()
                .data(data)
                .metadata(ResponseMetadata.success(message, status.value()))
                .build();
        return ResponseEntity.status(status).body(messageRes);
    }
}
