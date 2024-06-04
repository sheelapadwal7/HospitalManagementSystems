package com.hms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.userdetails.UserDetailsService;

import com.hms.dto.AppointmentDTO;
import com.hms.model.Appointment;
//import com.hms.security.JwtAuthenticationFilter;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@Configuration
public class ApplicationConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		// Configure ModelMapper to use strict matching strategy
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		// Explicitly map patientName property from Patient entity to patientName
		// property in AppointmentDTO
		modelMapper.createTypeMap(Appointment.class, AppointmentDTO.class).addMapping(src -> src.getPatient().getName(),
				AppointmentDTO::setPatientName);

		return modelMapper;
	}

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter()
//    {
//    	return new JwtAuthenticationFilter();
//    }
//    

}
