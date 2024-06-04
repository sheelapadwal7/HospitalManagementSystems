package com.hms.dto;

import lombok.Data;

@Data
public class PatientDTO {
	private String name;
	private int age;
	private String gender;
	private String address;
    private String contactNumber;
    private String email;
}
