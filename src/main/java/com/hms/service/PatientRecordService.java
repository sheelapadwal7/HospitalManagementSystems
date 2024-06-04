package com.hms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.hms.model.PatientRecord;
import com.hms.repository.PatientRecordRepository;
import com.hms.dto.PatientRecordDTO;
import com.hms.mapper.PatientRecordMapper;

@Service
public class PatientRecordService {

    @Autowired
    PatientRecordRepository patientRecordRepository;
    @Autowired
    PatientRecordMapper patientRecordMapper;

    public Page<PatientRecordDTO> getAllPatientRecords(Pageable pageable) {
        Page<PatientRecord> patientRecordPage = patientRecordRepository.findAll(pageable);
        return patientRecordPage.map(patientRecordMapper::toDto);
    }

    public List<PatientRecordDTO> getAllPatientRecords() {
        List<PatientRecord> patientRecords = patientRecordRepository.findAll();
        return patientRecords.stream().map(patientRecordMapper::toDto).collect(Collectors.toList());
    }

    public Optional<PatientRecordDTO> getPatientRecordById(Integer id) {
        Optional<PatientRecord> optionalPatientRecord = patientRecordRepository.findById(id);
        return optionalPatientRecord.map(patientRecordMapper::toDto);
    }

    public PatientRecordDTO createPatientRecord(PatientRecordDTO patientRecordDTO) {
        PatientRecord patientRecord = patientRecordMapper.toEntity(patientRecordDTO);
        patientRecord = patientRecordRepository.save(patientRecord);
        return patientRecordMapper.toDto(patientRecord);
    }

    public PatientRecordDTO updatePatientRecord(Integer id, PatientRecordDTO patientRecordDTO) {
        PatientRecord existingPatientRecord = patientRecordRepository.findById(id).orElse(null);
        if (existingPatientRecord == null) {
            return null; // or throw exception
        }
        PatientRecord updatedPatientRecord = patientRecordMapper.toEntity(patientRecordDTO);
        updatedPatientRecord.setId(id);
        updatedPatientRecord = patientRecordRepository.save(updatedPatientRecord);
        return patientRecordMapper.toDto(updatedPatientRecord);
    }

    public void deletePatientRecord(Integer id) {
        patientRecordRepository.deleteById(id);
    }

}
