package com.hms.dto;

import java.util.List;

import lombok.Data;

@Data
public class AdminResponseDTO {
    private boolean success;
    private String message;
    private List<String> errors;

    // Constructors
    public AdminResponseDTO(boolean success, String message, List<String> errors) {
        this.success = success;
        this.message = message;
        this.errors = errors;
    }

}

