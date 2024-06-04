package com.hms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.hms.model.Patient;
import com.hms.repository.PatientRepository;
import com.hms.specs.PatientSpecs;
import com.hms.dto.PatientDTO;
import com.hms.mapper.PatientMapper;

@Service
public class PatientService {

	@Autowired
	PatientRepository patientRepository;
	PatientMapper patientMapper;

	public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
		this.patientRepository = patientRepository;
		this.patientMapper = patientMapper;
	}

	
	public Page<PatientDTO> getAllPatients(Pageable pageable, String name, Integer age, String gender, String address) {
        Specification<Patient> specs = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            specs = specs.and(PatientSpecs.name(name));
        }

        if (age != null) {
            specs = specs.and(PatientSpecs.age(age));
        }

        if (gender != null && !gender.isEmpty()) {
            specs = specs.and(PatientSpecs.gender(gender));
        }

        if (address != null && !address.isEmpty()) {
            specs = specs.and(PatientSpecs.address(address));
        }

        Page<Patient> patientsPage = patientRepository.findAll(specs, pageable);
        return patientsPage.map(patientMapper::toDTO);
    }

	public List<PatientDTO> getAllPatients() {
		List<Patient> patients = patientRepository.findAll();
		return patients.stream().map(patientMapper::toDTO).collect(Collectors.toList());
	}

	public Page<PatientDTO> getAllPatients(Pageable pageable) {
		Page<Patient> patients = patientRepository.findAll(pageable);
		return patients.map(patientMapper::toDTO);
	}

	public Optional<PatientDTO> getPatientById(Integer id) {
		Optional<Patient> optionalPatient = patientRepository.findById(id);
		return optionalPatient.map(patientMapper::toDTO);
	}

	public PatientDTO createPatient(PatientDTO patientDTO) {
		Patient patient = patientMapper.toEntity(patientDTO);
		patient = patientRepository.save(patient);
		return patientMapper.toDTO(patient);
	}

	public PatientDTO updatePatient(Integer id, PatientDTO patientDTO) {
		Patient existingPatient = patientRepository.findById(id).orElse(null);
		if (existingPatient == null) {
			return null; // or throw exception
		}
		Patient updatedPatient = patientMapper.toEntity(patientDTO);
		updatedPatient.setId(id);
		updatedPatient = patientRepository.save(updatedPatient);
		return patientMapper.toDTO(updatedPatient);
	}

	public void deletePatient(Integer id) {
		patientRepository.deleteById(id);
	}

}
