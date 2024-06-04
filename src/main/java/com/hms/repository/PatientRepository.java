package com.hms.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.model.Patient;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
	
	Page<Patient> findAll(Specification<Patient> specs, Pageable pageable);
	 
	
	Optional<Patient> findById( Integer id);
}

