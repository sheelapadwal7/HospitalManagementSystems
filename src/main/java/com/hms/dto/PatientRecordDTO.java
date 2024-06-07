package com.hms.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRecordDTO {

 private Integer id;
 private String patientName;
 private String symptoms;
 private String diagnosis;
 private LocalDate date;

}
