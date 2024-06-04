package com.hms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.model.Appointment;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    
	List<Appointment> findByPatientId(Integer patientId);
}

