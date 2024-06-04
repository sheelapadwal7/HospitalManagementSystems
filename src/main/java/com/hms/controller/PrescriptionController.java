package com.hms.controller;

import com.hms.dto.PrescriptionDTO;
import com.hms.service.PrescriptionService;
import jakarta.validation.Valid;
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


@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    @Autowired
    PrescriptionService prescriptionService;

    @GetMapping("/all")
    public Page<PrescriptionDTO> getAllPrescriptions(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return prescriptionService.getAllPrescriptions(pageable);
    }

    @PostMapping
    public ResponseEntity<?> createPrescription(@Valid @RequestBody PrescriptionDTO prescriptionDTO,
                                                BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }
        PrescriptionDTO createdPrescription = prescriptionService.createPrescription(prescriptionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrescription);
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionDTO>> getAllPrescriptions() {
        List<PrescriptionDTO> prescriptions = prescriptionService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Integer id) {
        PrescriptionDTO prescriptionDTO = prescriptionService.getPrescriptionById(id);
        return prescriptionDTO != null ?
                ResponseEntity.ok(prescriptionDTO) :
                ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable Integer id,
                                                @Valid @RequestBody PrescriptionDTO prescriptionDTO,
                                                BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }
        PrescriptionDTO updatedPrescription = prescriptionService.updatePrescription(id, prescriptionDTO);
        if (updatedPrescription == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPrescription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable Integer id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
