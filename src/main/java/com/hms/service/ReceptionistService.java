package com.hms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.hms.model.Receptionist;
import com.hms.repository.ReceptionistRepository;
import com.hms.specs.ReceptionistSpecs;

import com.hms.dto.ReceptionistDTO;
import com.hms.mapper.ReceptionistMapper;

@Service
public class ReceptionistService {

	@Autowired
	ReceptionistRepository receptionistRepository;

	@Autowired
	ReceptionistMapper receptionistMapper;
	
	
	/*
	 * @Autowired JavaMailSender javaMailSender;
	 */

	@Autowired
	TokenLogService tokenlogservice;
	
	
	
	//@Autowired
	//CommunicationService communicationService;

	public Page<ReceptionistDTO> getAllReceptionists(Pageable pageable) {
		Page<Receptionist> receptionistPage = receptionistRepository.findAll(pageable);
		return receptionistPage.map(receptionistMapper::toDTO);
	}

	public Page<ReceptionistDTO> getAllReceptionists(Pageable pageable, String name, String department, String email,
			String phone) {
		Specification<Receptionist> specs = Specification.where(null);

		if (name != null && !name.isEmpty()) {
			specs = specs.and(ReceptionistSpecs.name(name));
		}

		if (department != null && !department.isEmpty()) {
			specs = specs.and(ReceptionistSpecs.department(department));
		}

		if (email != null && !email.isEmpty()) {
			specs = specs.and(ReceptionistSpecs.email(email));
		}

		if (phone != null && !phone.isEmpty()) {
			specs = specs.and(ReceptionistSpecs.phone(phone));
		}

		Page<Receptionist> receptionistsPage = receptionistRepository.findAll(specs, pageable);
		return receptionistsPage.map(receptionistMapper::toDTO);
	}

	public List<ReceptionistDTO> getAllReceptionists() {
		List<Receptionist> receptionists = receptionistRepository.findAll();
		return receptionists.stream().map(receptionistMapper::toDTO).collect(Collectors.toList());
	}

	public Optional<ReceptionistDTO> getReceptionistById(Integer id) {
		Optional<Receptionist> optionalReceptionist = receptionistRepository.findById(id);
		return optionalReceptionist.map(receptionistMapper::toDTO);
	}

	public ReceptionistDTO createReceptionist(ReceptionistDTO receptionistDTO) {
		Receptionist receptionist = receptionistMapper.toEntity(receptionistDTO);
		receptionist = receptionistRepository.save(receptionist);
		return receptionistMapper.toDTO(receptionist);
	}

	public ReceptionistDTO updateReceptionist(Integer id, ReceptionistDTO receptionistDTO) {
		Receptionist existingReceptionist = receptionistRepository.findById(id).orElse(null);
		if (existingReceptionist == null) {
			return null; // or throw exception
		}
		Receptionist updatedReceptionist = receptionistMapper.toEntity(receptionistDTO);
		updatedReceptionist.setId(id);
		updatedReceptionist = receptionistRepository.save(updatedReceptionist);
		return receptionistMapper.toDTO(updatedReceptionist);
	}

	public void deleteReceptionist(Integer id) {
		receptionistRepository.deleteById(id);
	}
	
	
	/*
	 * public void generateTokenAndSendEmail(String email) { Optional<Receptionist>
	 * studentOptional = receptionistRepository.findByEmail(email); if
	 * (studentOptional.isPresent()) { Receptionist student = studentOptional.get();
	 * 
	 * String token = tokenlogservice.generateToken(student);
	 * receptionistRepository.save(student);
	 * 
	 * 
	 * communicationService.sendResetEmail(student.getName() , student.getEmail(),
	 * token);
	 * 
	 * } else { System.out.println("Email not found"); } }
	 */
}
