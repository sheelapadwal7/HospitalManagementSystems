package com.hms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hms.model.Patient;
import com.hms.repository.PatientRepository;

import java.util.List;

@Component
public class Schedulars {

	@Autowired
	PatientRepository patientRepository;

	@Scheduled(cron = "0 0/5 * * * *") // Cron expression for  5 every minute
	public void sendReminders() {
		List<Patient> patients = patientRepository.findAll();
		for (Patient patient : patients) {
			// Send reminder email to each patient
			sendEmailToPatient(patient.getName());
		}
	}

	private void sendEmailToPatient(String patientName) {

		System.out.println("Appointment Reminder to:" + patientName);

	}
}
