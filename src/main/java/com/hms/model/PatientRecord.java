package com.hms.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class PatientRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String patientName;
	private String symptoms;
	private String diagnosis;
	private LocalDate date;
	
	
	@ManyToOne
	@JoinColumn(name = "patient_id")
	private Patient patient;
	
	@OneToOne
	@JoinColumn(name = "appointment_id")
	private Appointment appointment;



}
