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

import com.example.boilerplate.commons.dtos.MedicalRecordRequest;
import com.example.boilerplate.commons.models.Response;
import com.example.boilerplate.commons.models.ResponseMetadata;
import com.example.boilerplate.services.MedicalRecordService;

@RestController
@RequestMapping("medical-record")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MedicalRecordController {
    private Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    @Autowired
    private MedicalRecordService medicalRecordService;

    @PostMapping("create")
    public ResponseEntity<Response<?>> createMedicalRecord(@RequestBody MedicalRecordRequest request) {
        logger.info("Create medical record...");
        return this.response(this.medicalRecordService.createMedicalRecord(request), "Successfully created medical record", HttpStatus.CREATED);
    }

    @GetMapping("all")
    public ResponseEntity<Response<?>> getAllMedicalRecords() {
        logger.info("Get all medical records...");
        return this.response(this.medicalRecordService.getAllMedicalRecords(), "Successfully returned all medical records", HttpStatus.OK);
    }

    @GetMapping("{recordId}")
    public ResponseEntity<Response<?>> getMedicalRecord(@PathVariable UUID recordId) {
        logger.info("Get medical record... " + recordId);
        return this.response(this.medicalRecordService.getMedicalRecord(recordId), "Successfully returned medical record", HttpStatus.OK);
    }

    @PutMapping("{recordId}")
    public ResponseEntity<Response<?>> updateMedicalRecord(
            @PathVariable UUID recordId,
            @RequestBody MedicalRecordRequest request) {
        logger.info("Update medical record... " + recordId);
        return this.response(this.medicalRecordService.updateMedicalRecord(recordId, request), "Successfully updated medical record", HttpStatus.OK);
    }

    @DeleteMapping("{recordId}")
    public ResponseEntity<Response<?>> deleteMedicalRecord(@PathVariable UUID recordId) {
        logger.info("Delete medical record... " + recordId);
        this.medicalRecordService.deleteMedicalRecord(recordId);
        return this.response("Deleted medical record", "Successfully deleted medical record", HttpStatus.OK);
    }

    private ResponseEntity<Response<?>> response(Object data, String message, HttpStatus status) {
        Response<?> messageRes = Response.builder()
                .data(data)
                .metadata(ResponseMetadata.success(message, status.value()))
                .build();
        return ResponseEntity.status(status).body(messageRes);
    }
}
