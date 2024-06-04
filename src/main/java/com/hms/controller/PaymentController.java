package com.hms.controller;

import com.hms.dto.PaymentDTO;
import com.hms.service.PaymentService;

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
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/all")
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments();
    }
    
    @GetMapping
    public Page<PaymentDTO> getAllPayments(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return paymentService.getAllPayments(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Integer id) {
        Optional<PaymentDTO> paymentDTO = paymentService.getPaymentById(id);
        if (!paymentDTO.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(paymentDTO.get());
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentDTO paymentDTO, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }
        PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable Integer id, @Valid @RequestBody PaymentDTO paymentDTO,
                                           BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }
        PaymentDTO updatedPayment = paymentService.updatePayment(id, paymentDTO);
        if (updatedPayment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
