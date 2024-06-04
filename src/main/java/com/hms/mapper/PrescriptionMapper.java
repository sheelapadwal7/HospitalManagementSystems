package com.hms.mapper;

import com.hms.dto.PrescriptionDTO;
import com.hms.model.Prescription;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper {

    private final ModelMapper modelMapper;

    public PrescriptionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PrescriptionDTO toDto(Prescription prescription) {
        return modelMapper.map(prescription, PrescriptionDTO.class);
    }

    public Prescription toEntity(PrescriptionDTO prescriptionDTO) {
        return modelMapper.map(prescriptionDTO, Prescription.class);
    }
}
