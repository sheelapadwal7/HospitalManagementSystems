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

import com.hms.dto.PatientDTO;
import com.hms.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    PatientService patientService;

    @GetMapping
    public List<PatientDTO> getAllPatients() {
        return patientService.getAllPatients();
    }
    
    @GetMapping("/patient-records")
    public Page<PatientDTO> getAllPatientRecords(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return patientService.getAllPatients(pageable);
    }

    
    @GetMapping("/patients")
    public Page<PatientDTO> getAllPatients(Pageable pageable,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) Integer age,
                                           @RequestParam(required = false) String gender,
                                           @RequestParam(required = false) String address) {
        return patientService.getAllPatients(pageable, name, age, gender, address);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Integer id) {
        Optional<PatientDTO> patientDTO = patientService.getPatientById(id);
        if (!patientDTO.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(patientDTO.get());
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@Valid @RequestBody PatientDTO patientDTO, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }
        PatientDTO createdPatient = patientService.createPatient(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Integer id, @Valid @RequestBody PatientDTO patientDTO,
                                           BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }

        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
        if (updatedPatient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
