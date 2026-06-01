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

import com.example.boilerplate.commons.dtos.CaretakerRequest;
import com.example.boilerplate.commons.models.Response;
import com.example.boilerplate.commons.models.ResponseMetadata;
import com.example.boilerplate.services.CaretakerService;

@RestController
@RequestMapping("caretaker")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CaretakerController {
    private Logger logger = LoggerFactory.getLogger(CaretakerController.class);

    @Autowired
    private CaretakerService caretakerService;

    @PostMapping("create")
    public ResponseEntity<Response<?>> createCaretaker(@RequestBody CaretakerRequest request) {
        logger.info("Create caretaker...");
        return this.response(this.caretakerService.createCaretaker(request), "Successfully created caretaker", HttpStatus.CREATED);
    }

    @GetMapping("all")
    public ResponseEntity<Response<?>> getAllCaretakers() {
        logger.info("Get all caretakers...");
        return this.response(this.caretakerService.getAllCaretakers(), "Successfully returned all caretakers", HttpStatus.OK);
    }

    @GetMapping("{caretakerId}")
    public ResponseEntity<Response<?>> getCaretaker(@PathVariable UUID caretakerId) {
        logger.info("Get caretaker... " + caretakerId);
        return this.response(this.caretakerService.getCaretaker(caretakerId), "Successfully returned caretaker", HttpStatus.OK);
    }

    @PutMapping("{caretakerId}")
    public ResponseEntity<Response<?>> updateCaretaker(
            @PathVariable UUID caretakerId,
            @RequestBody CaretakerRequest request) {
        logger.info("Update caretaker... " + caretakerId);
        return this.response(this.caretakerService.updateCaretaker(caretakerId, request), "Successfully updated caretaker", HttpStatus.OK);
    }

    @DeleteMapping("{caretakerId}")
    public ResponseEntity<Response<?>> deleteCaretaker(@PathVariable UUID caretakerId) {
        logger.info("Delete caretaker... " + caretakerId);
        this.caretakerService.deleteCaretaker(caretakerId);
        return this.response("Deleted caretaker", "Successfully deleted caretaker", HttpStatus.OK);
    }

    private ResponseEntity<Response<?>> response(Object data, String message, HttpStatus status) {
        Response<?> messageRes = Response.builder()
                .data(data)
                .metadata(ResponseMetadata.success(message, status.value()))
                .build();
        return ResponseEntity.status(status).body(messageRes);
    }
}
