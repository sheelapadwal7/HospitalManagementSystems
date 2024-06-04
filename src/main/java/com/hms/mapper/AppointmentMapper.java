package com.hms.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.hms.dto.AppointmentDTO;
import com.hms.model.Appointment;

@Component
public class AppointmentMapper {

	private final ModelMapper modelMapper;

	public AppointmentMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public AppointmentDTO toDto(Appointment appointment) {
		return modelMapper.map(appointment, AppointmentDTO.class);
	}

	public Appointment toEntity(AppointmentDTO appointmentDTO) {
		return modelMapper.map(appointmentDTO, Appointment.class);
	}
}
