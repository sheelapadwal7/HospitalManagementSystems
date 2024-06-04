package com.hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}

