package com.hms.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AppointmentDTO {

    private Integer id;
    private LocalDateTime dateTime;
    private Integer patientId; 
    private String patientName;
    private String contactNumber;
    private boolean attended;
    

}
