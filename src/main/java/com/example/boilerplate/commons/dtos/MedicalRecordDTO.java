package com.example.boilerplate.commons.dtos;

import java.util.UUID;

public record MedicalRecordDTO(
        UUID id,
        String diagnosis,
        String treatment,
        String notes) {

}
