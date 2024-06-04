package com.hms.model;

import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "appointment")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDateTime dateTime;

	private String patientName;

	private String contactNumber;
	
	private boolean attended;

	@ManyToOne
	@JoinColumn(name = "patient_id")
	private Patient patient;

	@OneToOne(mappedBy = "appointment")
	private PatientRecord patientRecord;

}
