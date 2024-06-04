package com.hms.dto;

import java.util.Date;

import com.hms.enums.Gender;

import lombok.Data;

@Data
public class UserDTO {

	private Integer id;

	private String userName;

	private String firstName;

	private String middleName;

	private String lastName;

	private String email;

	private String contactNumber;

	private Gender gender;

	private Date date;

}
