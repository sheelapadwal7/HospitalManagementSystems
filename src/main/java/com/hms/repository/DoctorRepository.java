package com.hms.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.model.Doctor;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
	
	Page<Doctor> findAll(Specification<Doctor> specs, Pageable pageable);

	Doctor findByUserName(String userName);
}

