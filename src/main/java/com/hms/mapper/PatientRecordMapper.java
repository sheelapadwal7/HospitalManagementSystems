package com.hms.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.hms.model.PatientRecord;
import com.hms.dto.PatientRecordDTO;

@Component
public class PatientRecordMapper {

    private final ModelMapper modelMapper;

    public PatientRecordMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PatientRecordDTO toDto(PatientRecord patientRecord) {
        return modelMapper.map(patientRecord, PatientRecordDTO.class);
    }

    public PatientRecord toEntity(PatientRecordDTO patientRecordDTO) {
        return modelMapper.map(patientRecordDTO, PatientRecord.class);
    }


}
