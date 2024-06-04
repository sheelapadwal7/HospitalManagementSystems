package com.hms.mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.hms.model.Doctor;
import com.hms.dto.DoctorDTO;

@Component
public class DoctorMapper {

    private final ModelMapper modelMapper;

    public DoctorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DoctorDTO toDTO(Doctor doctor) {
        return modelMapper.map(doctor, DoctorDTO.class);
    }

    public Doctor toEntity(DoctorDTO doctorDTO) {
        return modelMapper.map(doctorDTO, Doctor.class);
    }
}



