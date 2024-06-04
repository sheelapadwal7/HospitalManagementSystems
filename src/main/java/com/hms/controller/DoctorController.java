package com.hms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.hms.dto.DoctorDTO;
import com.hms.service.DoctorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

	@Autowired
	DoctorService doctorService;

	@GetMapping
	public List<DoctorDTO> getAllDoctors() {
		return doctorService.getAllDoctors();
	}

	@GetMapping("/doctor-records")
	public Page<DoctorDTO> getAllDoctorRecords(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return doctorService.getAllDoctors(pageable);
	}

	@GetMapping("/doctors")
	public Page<DoctorDTO> getAllDoctors(Pageable pageable, @RequestParam(required = false) String name,
			@RequestParam(required = false) String specialization) {
		return doctorService.getAllDoctors(pageable, name, specialization);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getDoctorById(@PathVariable Integer id) {
		Optional<DoctorDTO> doctorDTO = doctorService.getDoctorById(id);
		if (doctorDTO == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(doctorDTO);
	}

	@PostMapping
	public ResponseEntity<?> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO, BindingResult result) {
		if (result.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error : result.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
		}
		DoctorDTO createdDoctor = doctorService.createDoctor(doctorDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDoctor);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateDoctor(@PathVariable Integer id, @Valid @RequestBody DoctorDTO doctorDTO,
			BindingResult result) {
		Map<String, String> errorMap = new HashMap<>();
		for (FieldError error : result.getFieldErrors()) {
			errorMap.put(error.getField(), error.getDefaultMessage());
		}
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
		}

		DoctorDTO updatedDoctor = doctorService.updateDoctor(id, doctorDTO);
		if (updatedDoctor == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updatedDoctor);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteDoctor(@PathVariable Integer id) {
		doctorService.deleteDoctor(id);
		return ResponseEntity.noContent().build();
	}
}
