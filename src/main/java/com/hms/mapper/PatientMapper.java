package com.hms.mapper;

import com.hms.dto.PatientDTO;
import com.hms.model.Patient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    private final ModelMapper modelMapper;

    public PatientMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PatientDTO toDTO(Patient patient) {
        return modelMapper.map(patient, PatientDTO.class);
    }

    public Patient toEntity(PatientDTO patientDTO) {
        return modelMapper.map(patientDTO, Patient.class);
    }
}
