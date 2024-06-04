package com.hms.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.model.Receptionist;


@Repository
public interface ReceptionistRepository extends JpaRepository<Receptionist, Integer> {

	Page<Receptionist> findAll(Specification<Receptionist> specs, Pageable pageable);
	
	
	Optional<Receptionist> findByName(String name);
	
	Optional<Receptionist> findByEmail(String email);
}
