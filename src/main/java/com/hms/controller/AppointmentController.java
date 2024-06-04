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

import com.hms.dto.AppointmentDTO;
import com.hms.model.AppointmentReminderRequest;
import com.hms.service.AppointmentService;
import com.hms.service.CommunicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

	@Autowired
	AppointmentService appointmentService;
	
	 @Autowired
	 CommunicationService communicationService;

	@GetMapping
	public List<AppointmentDTO> getAllAppointments() {
		return appointmentService.getAllAppointments();
	}

	@GetMapping("/appointment-records")
	public Page<AppointmentDTO> getAllAppointmentRecords(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return appointmentService.getAllAppointments(pageable);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getAppointmentById(@PathVariable Integer id) {
		Optional<AppointmentDTO> appointmentDTO = appointmentService.getAppointmentById(id);
		if (!appointmentDTO.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(appointmentDTO.get());
	}

	@PostMapping
	public ResponseEntity<?> createAppointment(@RequestParam Integer patientId,
			@Valid @RequestBody AppointmentDTO appointmentDTO, BindingResult result) {
		if (result.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error : result.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
		}

		// Ensure that patientId is set
		if (patientId == null) {
			Map<String, String> errorMap = new HashMap<>();
			errorMap.put("patientId", "Patient ID must not be null");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
		}

		AppointmentDTO createdAppointment = appointmentService.createAppointment(patientId, appointmentDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateAppointment(@PathVariable Integer id,
			@Valid @RequestBody AppointmentDTO appointmentDTO, BindingResult result) {
		Map<String, String> errorMap = new HashMap<>();
		for (FieldError error : result.getFieldErrors()) {
			errorMap.put(error.getField(), error.getDefaultMessage());
		}
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
		}

		AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentDTO);
		if (updatedAppointment == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(updatedAppointment);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAppointment(@PathVariable Integer id) {
		appointmentService.deleteAppointment(id);
		return ResponseEntity.noContent().build();
	}

	 @PutMapping("/mark-attended/{appointmentId}")
	    public ResponseEntity<AppointmentDTO> markAppointmentAsAttended(
	            @PathVariable Integer appointmentId
	            ) {
	        AppointmentDTO updatedAppointmentDTO = appointmentService.markAppointmentAsAttended(appointmentId);
	        if (updatedAppointmentDTO != null) {
	            return new ResponseEntity<>(updatedAppointmentDTO, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	 
	 @PostMapping("/sendReminder")
	    public String sendAppointmentReminder(@RequestBody AppointmentReminderRequest request) {
	        communicationService.sendAppointmentReminder(request.getPatientEmail(), request.getAppointmentDetails());
	        return "Reminder sent successfully";
	    }
}
