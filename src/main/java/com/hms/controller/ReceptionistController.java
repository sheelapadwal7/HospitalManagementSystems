package com.hms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hms.dto.ReceptionistDTO;
import com.hms.service.ReceptionistService;

import java.util.List;

@RestController
@RequestMapping("/receptionists")
public class ReceptionistController {
	
	@Autowired
	ReceptionistService receptionistService;

	@GetMapping("/receptionists")
	public Page<ReceptionistDTO> getAllReceptionists(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return receptionistService.getAllReceptionists(pageable);
	}
	
	 @GetMapping("/allreceptionists")
	    public Page<ReceptionistDTO> getAllReceptionists(Pageable pageable,
	                                                     @RequestParam(required = false) String name,
	                                                     @RequestParam(required = false) String department,
	                                                     @RequestParam(required = false) String email,
	                                                     @RequestParam(required = false) String phone) {
	        return receptionistService.getAllReceptionists(pageable, name, department, email, phone);
	    }

	@GetMapping
	public ResponseEntity<List<ReceptionistDTO>> getAllReceptionists() {
		List<ReceptionistDTO> receptionists = receptionistService.getAllReceptionists();
		return new ResponseEntity<>(receptionists, HttpStatus.OK);
	}

	@GetMapping("/receptionists/{id}")
    public ResponseEntity<ReceptionistDTO> getReceptionistById(@PathVariable Integer id) {
        ReceptionistDTO receptionist = receptionistService.getReceptionistById(id).get();
        return ResponseEntity.ok(receptionist);
    }

	@PostMapping
	public ResponseEntity<ReceptionistDTO> addReceptionist(@RequestBody ReceptionistDTO receptionistDTO) {
		ReceptionistDTO savedReceptionist = receptionistService.createReceptionist(receptionistDTO);
		return new ResponseEntity<>(savedReceptionist, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ReceptionistDTO> updateReceptionist(@PathVariable Integer id,
			@RequestBody ReceptionistDTO receptionistDTO) {
		ReceptionistDTO updatedReceptionist = receptionistService.updateReceptionist(id, receptionistDTO);
		return new ResponseEntity<>(updatedReceptionist, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReceptionist(@PathVariable Integer id) {
		receptionistService.deleteReceptionist(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
