package com.hms.model;

import lombok.Data;

@Data
public class AppointmentReminderRequest {

	private String patientEmail;
	private String appointmentDetails;

}
