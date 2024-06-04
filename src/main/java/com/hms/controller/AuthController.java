package com.hms.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hms.dto.LoginRequestDTO;
import com.hms.dto.LoginResponseDTO;
import com.hms.dto.UserDTO;
import com.hms.model.Admin;
import com.hms.model.Doctor;
import com.hms.security.JwtService;
import com.hms.service.AdminService;
import com.hms.service.DoctorService;
import com.hms.service.ReceptionistService;
import com.hms.service.TokenLogService;

@RestController

@RequestMapping("/auth")
public class AuthController {

	@Autowired
	TokenLogService tokenlogservice;

	@Autowired
	JwtService jwtService;

	@Autowired
	AdminService adminService;

	@Autowired
	DoctorService doctorService;

	@Autowired
	ReceptionistService receptionistService;

	@GetMapping("converttohash")
	public String convertToHash(@RequestParam String clearText) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String cipherText = passwordEncoder.encode(clearText);

		return cipherText;

	}

	@PostMapping("/admin/login")
	public LoginResponseDTO adminLogin(@RequestBody LoginRequestDTO loginRequestDto) {

		LoginResponseDTO loginResponseDto = new LoginResponseDTO();

		if (loginRequestDto.getUserName() == null || loginRequestDto.getUserName().isEmpty()
				|| loginRequestDto.getPassword() == null || loginRequestDto.getPassword().isEmpty()) {
			loginResponseDto.setStatus(false);
			loginResponseDto.setMessage("Username or password cannot be empty");
			return loginResponseDto;
		}

		Admin admin = adminService.findAdminByUsername(loginRequestDto.getUserName());

		if (!adminService.isAdminValid(admin)) {

			return adminService.createInvalidAdminResponse(loginResponseDto, admin);
		}

		// Check if password matches
		boolean passwordMatches = adminService.verifyPassword(loginRequestDto.getPassword(), admin.getPassword());
		if (!passwordMatches) { // Increment login attempts
			adminService.handleIncorrectPassword(loginResponseDto, admin);
			return loginResponseDto;
		}

		// Reset login attempts upon successful login
		adminService.resetLoginAttempts(admin);

		// Check if account is locked
		if (admin.getAccountStatus().equals("locked")) {
			LocalDateTime currentTime = LocalDateTime.now();
			LocalDateTime unlockTime = admin.getLockedDateTime().plusHours(24);
			if (currentTime.isBefore(unlockTime)) {
				throw new RuntimeException("Account locked. Please try again later.");
			}
		
		else {
				admin.setAccountStatus("active");
				admin.setLoginAttempts(0);
				admin.setLockedDateTime(null);
			}
		}

		// Check password
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (passwordEncoder.matches(loginRequestDto.getPassword(), admin.getPassword())) {

			// Reset login attempts and
			// update last login
			admin.setLoginAttempts(0);
			admin.setLockedDateTime(LocalDateTime.now());

			// Generate token
			String token = jwtService.generateToken(admin, loginRequestDto.getUserName());
			// String token = tokenlogservice.generateToken(admin);

			// Response preparation
			UserDTO userDto = new UserDTO();
			userDto.setFirstName(admin.getFirstName());
			userDto.setUserName(admin.getUsername());

			loginResponseDto.setStatus(true);
			loginResponseDto.setMessage("admin Login Successfully");
			loginResponseDto.setUser(userDto);
			loginResponseDto.setToken(token);

		}

		// Response send
		return loginResponseDto;

	}

	@PostMapping("/admin/logout")
	public ResponseEntity<String> logout(@RequestParam String token) {
		if (jwtService.inValidateToken(token)) {
			return ResponseEntity.ok("Logout successfully");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
		}
	}

	@PostMapping("/doctor/login")
	public LoginResponseDTO doctorLogin(@RequestBody LoginRequestDTO loginRequestDto) {

		LoginResponseDTO loginResponseDto = new LoginResponseDTO();

		if (loginRequestDto.getUserName() == null || loginRequestDto.getUserName().isEmpty()
				|| loginRequestDto.getPassword() == null || loginRequestDto.getPassword().isEmpty()) {
			loginResponseDto.setStatus(false);
			loginResponseDto.setMessage("Username or password cannot be empty");
			return loginResponseDto;
		}

		Doctor doctor = doctorService.findDoctorByUsername(loginRequestDto.getUserName());

		if (!doctorService.isDoctorValid(doctor)) {

			return doctorService.createInvalidDoctorResponse(loginResponseDto, doctor);
		}

		// Check if password matches
		boolean passwordMatches = doctorService.verifyPassword(loginRequestDto.getPassword(), doctor.getPassword());
		if (!passwordMatches) {
			// Increment login attempts
			doctorService.handleIncorrectPassword(loginResponseDto, doctor);
			return loginResponseDto;
		}

		// Reset login attempts upon successful login
		doctorService.resetLoginAttempts(doctor);

		// Check if account is locked
		if (doctor.getAccountStatus().equals("locked")) {
			LocalDateTime currentTime = LocalDateTime.now();
			LocalDateTime unlockTime = doctor.getLockedDateTime().plusHours(24);
			if (currentTime.isBefore(unlockTime)) {
				throw new RuntimeException("Account locked. Please try again later.");
			} else {
				doctor.setAccountStatus("active");
				doctor.setLoginAttempts(0);
				doctor.setLockedDateTime(null);
			}
		}

		// Check password
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (passwordEncoder.matches(loginRequestDto.getPassword(), doctor.getPassword())) {

			// Reset login attempts and // update last login
			doctor.setLoginAttempts(0);
			doctor.setLockedDateTime(LocalDateTime.now());

			// Generate token
			String token = jwtService.generateToken2(doctor, loginRequestDto.getUserName());
			// String token = tokenlogservice.generateToken(doctor);

			// Response preparation
			UserDTO userDto = new UserDTO();
			userDto.setFirstName(doctor.getName());
			userDto.setUserName(doctor.getUserName());

			loginResponseDto.setStatus(true);
			loginResponseDto.setMessage("Doctor Login Successfully");
			loginResponseDto.setUser(userDto);
			loginResponseDto.setToken(token);

		}

		// Response send
		return loginResponseDto;

	}
	
	@PostMapping("/doctor/logout")
	public ResponseEntity<String> doctorlogout(@RequestParam String token) {
		if (jwtService.inValidateToken(token)) {
			return ResponseEntity.ok("Logout successfully");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
		}
	}

	/*
	 * @PostMapping("/sendEmail") public ResponseEntity<String>
	 * forgotPassword(@RequestParam String email) {
	 * receptionistService.generateTokenAndSendEmail(email); return
	 * ResponseEntity.ok("Password reset link sent to your email"); }
	 */
}
