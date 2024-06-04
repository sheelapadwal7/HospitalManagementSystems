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

import com.hms.dto.InventoryDTO;
import com.hms.service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @GetMapping
    public List<InventoryDTO> getAllInventory() {
        return inventoryService.getAllInventory();
    }
    
    @GetMapping("/inventory-records")
    public Page<InventoryDTO> getAllInventoryRecords(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return inventoryService.getAllInventory(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInventoryById(@PathVariable Integer id) {
        Optional<InventoryDTO> inventoryDTO = inventoryService.getInventoryById(id);
        if (!inventoryDTO.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inventoryDTO.get());
    }

    @PostMapping
    public ResponseEntity<?> createInventory(@Valid @RequestBody InventoryDTO inventoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }
        InventoryDTO createdInventory = inventoryService.createInventory(inventoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable Integer id, @Valid @RequestBody InventoryDTO inventoryDTO,
                                              BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }

        InventoryDTO updatedInventory = inventoryService.updateInventory(id, inventoryDTO);
        if (updatedInventory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedInventory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable Integer id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}
