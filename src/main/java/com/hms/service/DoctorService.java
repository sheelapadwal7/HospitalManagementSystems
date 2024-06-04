package com.hms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import com.hms.model.Admin;
import com.hms.model.Doctor;
import com.hms.repository.DoctorRepository;
import com.hms.specs.DoctorSpecs;
import com.hms.dto.DoctorDTO;
import com.hms.dto.LoginResponseDTO;
import com.hms.mapper.DoctorMapper;

@Service
public class DoctorService {

	@Autowired
	DoctorRepository doctorRepository;
	@Autowired
	DoctorMapper doctorMapper;

	public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
		Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
		return doctorPage.map(doctorMapper::toDTO);
	}

	public Page<DoctorDTO> getAllDoctors(Pageable pageable, String name, String specialization) {
		Specification<Doctor> specs = Specification.where(null);

		if (name != null && !name.isEmpty()) {
			specs = specs.and(DoctorSpecs.name(name));
		}

		if (specialization != null && !specialization.isEmpty()) {
			specs = specs.and(DoctorSpecs.specialization(specialization));
		}

		Page<Doctor> doctorsPage = doctorRepository.findAll(specs, pageable);
		return doctorsPage.map(doctorMapper::toDTO);
	}

	public List<DoctorDTO> getAllDoctors() {
		List<Doctor> doctors = doctorRepository.findAll();
		return doctors.stream().map(doctorMapper::toDTO).collect(Collectors.toList());
	}

	public Optional<DoctorDTO> getDoctorById(Integer id) {
		Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
		return optionalDoctor.map(doctorMapper::toDTO);

	}

	public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
		Doctor doctor = doctorMapper.toEntity(doctorDTO);
		doctor = doctorRepository.save(doctor);
		return doctorMapper.toDTO(doctor);
	}

	public DoctorDTO updateDoctor(Integer id, DoctorDTO doctorDTO) {
		Doctor existingDoctor = doctorRepository.findById(id).orElse(null);
		if (existingDoctor == null) {
			return null; // or throw exception
		}
		Doctor updatedDoctor = doctorMapper.toEntity(doctorDTO);
		updatedDoctor.setId(id);
		updatedDoctor = doctorRepository.save(updatedDoctor);
		return doctorMapper.toDTO(updatedDoctor);
	}

	public void deleteDoctor(Integer id) {
		doctorRepository.deleteById(id);
	}

	
	
	 
	 
	 public boolean isDoctorValid(Doctor doctor) {
			return doctor != null && !"LOCKED".equals(doctor.getAccountStatus());
		}

		public void handleIncorrectPassword(LoginResponseDTO loginResponseDto, Doctor doctor) {
			int loginAttempts = doctor.getLoginAttempts() + 1;
			doctor.setLoginAttempts(loginAttempts);

			if (loginAttempts >= 3) {
				lockAccount(doctor);
				loginResponseDto.setMessage("Incorrect password. Account is locked due to multiple login attempts.");
			} else {
				saveDoctor(doctor);
				loginResponseDto.setMessage("Incorrect password. Please try again.");
			}
			loginResponseDto.setStatus(false);
		}

		public void lockAccount(Doctor doctor) {
			doctor.setAccountStatus("LOCKED");
			doctor.setLockedDateTime(LocalDateTime.now());
			saveDoctor(doctor);
		}

		
		public void saveDoctor(Doctor doctor) {
			doctorRepository.save(doctor);
		}
		public LoginResponseDTO createInvalidDoctorResponse(LoginResponseDTO loginResponseDto, Doctor doctor) {
			loginResponseDto.setStatus(false);

			String message = "Account is locked. Please try again after 24 hours.";
			if ("LOCKED".equals(doctor.getAccountStatus()) && isAccountLocked(doctor)) {
				doctor.setAccountStatus("ACTIVE");
				doctor.setLoginAttempts(0);
				saveDoctor(doctor);
				message = "Account unlocked. Please try again.";
			}

			loginResponseDto.setMessage(message);
			return loginResponseDto;
		}

		public boolean isAccountLocked(Doctor doctor) {
			LocalDateTime lockDateTime = doctor.getLockedDateTime();
			LocalDateTime currentDateTime = LocalDateTime.now();
			return lockDateTime.plusHours(24).isBefore(currentDateTime);
		}

		public Doctor findDoctorByUsername(String userName) {
			return doctorRepository.findByUserName(userName);
		}

		public boolean verifyPassword(String rawPassword, String encodedPassword) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			return passwordEncoder.matches(rawPassword, encodedPassword);
		}

		
		public void resetLoginAttempts(Doctor doctor) {
			doctor.setLoginAttempts(0);
			saveDoctor(doctor);
		}

		public boolean passwordMatches(String rawPassword, String encodedPassword) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			return passwordEncoder.matches(rawPassword, encodedPassword);
		}

		public void incrementLoginAttempts(Doctor doctor) {
			doctor.setLoginAttempts(doctor.getLoginAttempts() + 1);
			saveDoctor(doctor);
		}


}
