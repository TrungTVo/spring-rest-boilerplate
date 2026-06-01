package com.example.boilerplate.commons.dtos;

public record MedicalRecordRequest(
        String diagnosis,
        String treatment,
        String notes) {

}
