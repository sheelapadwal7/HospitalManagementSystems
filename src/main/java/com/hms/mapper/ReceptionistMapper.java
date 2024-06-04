package com.hms.mapper;

import com.hms.dto.ReceptionistDTO;
import com.hms.model.Receptionist;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReceptionistMapper {

    private final ModelMapper modelMapper;

    public ReceptionistMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReceptionistDTO toDTO(Receptionist receptionist) {
        return modelMapper.map(receptionist, ReceptionistDTO.class);
    }

    public Receptionist toEntity(ReceptionistDTO receptionistDTO) {
        return modelMapper.map(receptionistDTO, Receptionist.class);
    }
}
