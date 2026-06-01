package com.example.boilerplate.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.boilerplate.commons.dtos.HabitatRequest;
import com.example.boilerplate.commons.models.Response;
import com.example.boilerplate.commons.models.ResponseMetadata;
import com.example.boilerplate.services.HabitatService;

@RestController
@RequestMapping("habitat")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HabitatController {
    private Logger logger = LoggerFactory.getLogger(HabitatController.class);

    @Autowired
    private HabitatService habitatService;

    @PostMapping("create")
    public ResponseEntity<Response<?>> createHabitat(@RequestBody HabitatRequest request) {
        logger.info("Create habitat...");
        return this.response(this.habitatService.createHabitat(request), "Successfully created habitat", HttpStatus.CREATED);
    }

    @GetMapping("all")
    public ResponseEntity<Response<?>> getAllHabitats() {
        logger.info("Get all habitats...");
        return this.response(this.habitatService.getAllHabitats(), "Successfully returned all habitats", HttpStatus.OK);
    }

    @GetMapping("{habitatId}")
    public ResponseEntity<Response<?>> getHabitat(@PathVariable UUID habitatId) {
        logger.info("Get habitat... " + habitatId);
        return this.response(this.habitatService.getHabitat(habitatId), "Successfully returned habitat", HttpStatus.OK);
    }

    @PutMapping("{habitatId}")
    public ResponseEntity<Response<?>> updateHabitat(
            @PathVariable UUID habitatId,
            @RequestBody HabitatRequest request) {
        logger.info("Update habitat... " + habitatId);
        return this.response(this.habitatService.updateHabitat(habitatId, request), "Successfully updated habitat", HttpStatus.OK);
    }

    @DeleteMapping("{habitatId}")
    public ResponseEntity<Response<?>> deleteHabitat(@PathVariable UUID habitatId) {
        logger.info("Delete habitat... " + habitatId);
        this.habitatService.deleteHabitat(habitatId);
        return this.response("Deleted habitat", "Successfully deleted habitat", HttpStatus.OK);
    }

    private ResponseEntity<Response<?>> response(Object data, String message, HttpStatus status) {
        Response<?> messageRes = Response.builder()
                .data(data)
                .metadata(ResponseMetadata.success(message, status.value()))
                .build();
        return ResponseEntity.status(status).body(messageRes);
    }
}
