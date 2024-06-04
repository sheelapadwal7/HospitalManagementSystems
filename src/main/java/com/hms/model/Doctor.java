package com.hms.model;

import lombok.Data;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;


@Entity
@Data
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name cannot be empty") 
    private String name;

    @NotEmpty(message = "Specialization cannot be empty") 
    private String specialization;
    
    private String password;
    private String userName;
    private String accountStatus;
   	private Integer loginAttempts;
   	private LocalDateTime lockedDateTime;
}
