package com.example.boilerplate.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.boilerplate.commons.dtos.AnimalDTOMapper;
import com.example.boilerplate.commons.dtos.MedicalRecordDTO;
import com.example.boilerplate.commons.dtos.MedicalRecordRequest;
import com.example.boilerplate.commons.models.MedicalRecord;
import com.example.boilerplate.exceptions.NoRecordFoundException;
import com.example.boilerplate.repositories.MedicalRecordRepository;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AnimalDTOMapper animalDTOMapper;

    @Transactional(rollbackFor = Exception.class)
    public MedicalRecordDTO createMedicalRecord(MedicalRecordRequest request) {
        MedicalRecord medicalRecord = new MedicalRecord(request.diagnosis(), request.treatment(), request.notes());
        return this.animalDTOMapper.toMedicalRecordDTO(this.medicalRecordRepository.save(medicalRecord));
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<MedicalRecordDTO> getAllMedicalRecords() {
        return this.medicalRecordRepository.findAll()
                .stream()
                .map(this.animalDTOMapper::toMedicalRecordDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public MedicalRecordDTO getMedicalRecord(UUID recordId) {
        return this.animalDTOMapper.toMedicalRecordDTO(this.findMedicalRecord(recordId));
    }

    @Transactional(rollbackFor = Exception.class)
    public MedicalRecordDTO updateMedicalRecord(UUID recordId, MedicalRecordRequest request) {
        MedicalRecord medicalRecord = this.findMedicalRecord(recordId);
        medicalRecord.setDiagnosis(request.diagnosis());
        medicalRecord.setTreatment(request.treatment());
        medicalRecord.setNotes(request.notes());
        return this.animalDTOMapper.toMedicalRecordDTO(this.medicalRecordRepository.save(medicalRecord));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMedicalRecord(UUID recordId) {
        MedicalRecord medicalRecord = this.findMedicalRecord(recordId);
        if (medicalRecord.getAnimal() != null) {
            medicalRecord.getAnimal().setMedicalRecord(null);
            medicalRecord.setAnimal(null);
        }
        this.medicalRecordRepository.delete(medicalRecord);
    }

    private MedicalRecord findMedicalRecord(UUID recordId) {
        return this.medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new NoRecordFoundException("No medical record found with id " + recordId));
    }
}
