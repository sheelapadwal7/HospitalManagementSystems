package com.hms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.dto.LoginRequestDTO;
import com.hms.dto.LoginResponseDTO;
import com.hms.model.Admin;
import com.hms.repository.AdminRepository;


@Service
public class AdminService {

	@Autowired
	AdminRepository adminRepository;

	public Admin findAdminByUsername(String username) {
		return adminRepository.findByUserName(username).orElse(null);
	}

	public List<String> validate(Admin admin) {
		List<String> error = new ArrayList<>();
		boolean isEmail = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matcher(admin.getEmail())
				.matches();

		if (!isEmail) {
			error.add("email is not correct");
		}

		if (admin.getFirstName() == null) {
			error.add("Required first name");
		}

		if (admin.getLastName() == null) {
			error.add("required last name");
		}

		Pattern passwordPattern = Pattern
				.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
		Matcher passwordMatcher = passwordPattern.matcher(admin.getPassword());
		if (!passwordMatcher.matches()) {
			error.add("Password is not correct");
		}

		return error;
	}

	public Admin AddAdmin(Admin admin) {

		return adminRepository.save(admin);

	}
	
	public Admin addAdmin(Admin admin) {

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = bCryptPasswordEncoder.encode(admin.getPassword());
		admin.setPassword(hashedPassword);

		// Save the student to the database Student savedStudent =
		return adminRepository.save(admin);

	}

	public List<Admin> getAdmin() {

		return adminRepository.findAll();
	}

	public Optional<Admin> getAdminById(Integer adminId) {

		return adminRepository.findById(adminId);

	}

	// Delete Admin by id
	public boolean deleteAdmin(Integer id) {
		boolean exists = adminRepository.existsById(id);
		if (exists) {
			adminRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	// update admin by id
	public Admin updateAdmin(int requirementid, Admin admin) {

		admin.setId(requirementid);
		return adminRepository.save(admin);

	}

	public Admin login(LoginRequestDTO loginRequestDto) {
		System.out.println("3");
		Optional<Admin> admino = adminRepository.findByUserName(loginRequestDto.getUserName());

		System.out.println(admino);
		Admin admin = null;

		if (admino.isPresent()) {

			// Admin admindb = admino.get();

			// BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//        	System.out.print("passwrod user: " + loginRequestDto.getPassword() + " from db:" + studentdb.getPassword());
			// if (passwordEncoder.matches(loginRequestDto.getPassword(),
			// admindb.getPassword())) {
			// admin = admindb;
			// }

		}

		return admin;
	}

	public void resetLoginAttempts(Admin admin) {
		admin.setLoginAttempts(0);
		saveAdmin(admin);
	}

	public boolean passwordMatches(String rawPassword, String encodedPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	public void incrementLoginAttempts(Admin admin) {
		admin.setLoginAttempts(admin.getLoginAttempts() + 1);
		saveAdmin(admin);
	}

	public boolean isAdminValid(Admin admin) {
		return admin != null && !"LOCKED".equals(admin.getAccountStatus());
	}

	public void handleIncorrectPassword(LoginResponseDTO loginResponseDto, Admin admin) {
		int loginAttempts = admin.getLoginAttempts() + 1;
		admin.setLoginAttempts(loginAttempts);

		if (loginAttempts >= 3) {
			lockAccount(admin);
			loginResponseDto.setMessage("Incorrect password. Account is locked due to multiple login attempts.");
		} else {
			saveAdmin(admin);
			loginResponseDto.setMessage("Incorrect password. Please try again.");
		}
		loginResponseDto.setStatus(false);
	}

	public void lockAccount(Admin admin) {
		admin.setAccountStatus("LOCKED");
		admin.setLockedDateTime(LocalDateTime.now());
		saveAdmin(admin);
	}

	public LoginResponseDTO createInvalidAdminResponse(LoginResponseDTO loginResponseDto, Admin admin) {
		loginResponseDto.setStatus(false);

		String message = "Account is locked. Please try again after 24 hours.";
		if ("LOCKED".equals(admin.getAccountStatus()) && isAccountLocked(admin)) {
			admin.setAccountStatus("ACTIVE");
			admin.setLoginAttempts(0);
			saveAdmin(admin);
			message = "Account unlocked. Please try again.";
		}

		loginResponseDto.setMessage(message);
		return loginResponseDto;
	}

	public boolean isAccountLocked(Admin admin) {
		LocalDateTime lockDateTime = admin.getLockedDateTime();
		LocalDateTime currentDateTime = LocalDateTime.now();
		return lockDateTime.plusHours(24).isBefore(currentDateTime);
	}

	public Admin findStudentByUsername(String username) {
		return adminRepository.findByUserName(username).orElse(null);
	}

	public boolean verifyPassword(String rawPassword, String encodedPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	public void saveAdmin(Admin admin) {
		adminRepository.save(admin);
	}

	public String generateFileName(String originalFileName) {
		String randomString = UUID.randomUUID().toString().substring(0, 8); // Generate a random string
		String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.')); // Get file extension
		return "image" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "_" + randomString
				+ fileExtension;
	}

}
