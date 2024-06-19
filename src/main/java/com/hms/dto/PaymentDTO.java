package com.hms.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PaymentDTO {

	private Integer id;
	private String patientName;
	private String paymentMethod;
	private double amount;
	private Date paymentDate;

}
