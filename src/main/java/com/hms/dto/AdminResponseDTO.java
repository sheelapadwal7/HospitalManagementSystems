package com.hms.dto;

import java.util.List;

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

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

