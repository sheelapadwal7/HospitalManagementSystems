package com.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO

{

	private boolean status;
	private String message;

	private UserDTO user;

	private String token;

	public LoginResponseDTO(boolean status, String message, String token) {
		super();
		this.status = status;
		this.message = message;
		this.token = token;
	}

}
