
package com.hms.controller;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hms.model.Admin;
import com.hms.repository.AdminRepository;
import com.hms.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private static final String UPLOAD_DIR = "C:\\Users\\Sheela\\Documents\\workspace-spring-tool-suite-4-4.22.0.RELEASE\\HospitalManagementSystem\\src\\main\\resources\\static\\Assets\\";

	@Autowired
	AdminService adminService;

	@Autowired
	AdminRepository adminRepository;

	@GetMapping("/all")
	public ResponseEntity<?> getAdmin() {
		List<Admin> adminCount = adminService.getAdmin();
		if (adminCount.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(adminService.getAdmin(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getAdminById(@PathVariable Integer id) {
		Optional<Admin> adminOptional = adminService.getAdminById(id);
		if (adminOptional.isEmpty()) {

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {

			Admin admin = adminOptional.get();

			return ResponseEntity.ok().body(admin);
		}
	}

	@PostMapping("/add")
	public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {

		List<String> error = adminService.validate(admin);
		if (!error.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

		}
		adminService.addAdmin(admin);
		return ResponseEntity.ok().body("Admin added successfully.");

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> UpdateAdmin(@PathVariable Integer id, @RequestBody Admin admin) {

		List<String> error = adminService.validate(admin);
		if (error.size() != 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		}
		adminService.updateAdmin(id, admin);
		return ResponseEntity.ok().body("Admin Updated Succesfully");

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAdmin(@PathVariable Integer id) {

		boolean deleted = adminService.deleteAdmin(id);

		if (deleted) {
			return ResponseEntity.ok("Admin with ID " + id + " deleted successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("admin with ID " + id + " not found.");
		}
	}

	@PostMapping("/uploadProfileImage/{id}")
	public String updateAdminProfile(@PathVariable Integer id, @RequestPart(name = "file") MultipartFile file) {

		// Validate file type
		if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
			return "Error: File type must be JPEG or PNG.";
		}

		// Find admin by ID
		Optional<Admin> adminOptional = adminRepository.findById(id);
		if (adminOptional.isEmpty()) {
			return "Error: Admin with ID " + id + " not found.";
		}

		// Extracting the Admin object from Optional
		Admin admin = adminOptional.get();

		try {
			// Save file to the upload directory
			String assetsFolderPath = UPLOAD_DIR;

			String fileName = adminService.generateFileName(file.getOriginalFilename());
			String filePath = assetsFolderPath + fileName;
			File dest = new File(filePath);
			file.transferTo(dest);

			// Update admin's image path
			admin.setImagePath(fileName);
			adminRepository.save(admin);

			return "Admin profile Image updated successfully.";
		} catch (IOException e) {
			e.printStackTrace();
			return "Error occurred while saving the file.";
		}
	}

	@GetMapping(value = "/admin/getProfileImage/{id}", produces = { MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE })
	public ResponseEntity<byte[]> getProfileImage(@PathVariable Integer id) {

		Optional<Admin> adminOptional = adminRepository.findById(id);
		if (adminOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Admin admin = adminOptional.get();

		try {
			// Read the image file
			Path imagePath = Paths.get(UPLOAD_DIR, admin.getImagePath());
			byte[] imageBytes = Files.readAllBytes(imagePath);

			// Return the image bytes
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
