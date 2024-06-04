package com.hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.model.PatientRecord;

@Repository
public interface PatientRecordRepository extends JpaRepository<PatientRecord, Integer> {
}

