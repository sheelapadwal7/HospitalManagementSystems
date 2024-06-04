package com.hms.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DoctorDTO {
	private Integer id;
    private String name;
    private String specialization;
    private String userName;
    private String password;
    private String accountStatus;
    private Integer loginAttempts;
    private LocalDateTime lockedDateTime;
    
	
}

