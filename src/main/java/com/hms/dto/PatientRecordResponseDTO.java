package com.hms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRecordResponseDTO {
	private boolean success;
	private String message;
	private List<String> errors;

}
