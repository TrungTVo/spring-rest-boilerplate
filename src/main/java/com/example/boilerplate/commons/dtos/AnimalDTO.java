package com.example.boilerplate.commons.dtos;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Animal DTO to exclude {@code password} field in the HTTP Response
 */
public record AnimalDTO(
        UUID id,
        String name,
        int age,
        BigDecimal balance,
        HabitatDTO habitat,
        MedicalRecordDTO medicalRecord,
        List<CaretakerDTO> caretakers
) {

}
