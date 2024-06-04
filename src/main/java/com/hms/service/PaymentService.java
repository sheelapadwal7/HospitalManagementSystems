package com.hms.service;

import com.hms.dto.PaymentDTO;
import com.hms.mapper.PaymentMapper;
import com.hms.model.Payment;
import com.hms.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

	@Autowired
	PaymentRepository paymentRepository;
	@Autowired
	PaymentMapper paymentMapper;

	public List<PaymentDTO> getAllPayments() {
		List<Payment> payments = paymentRepository.findAll();
		return payments.stream().map(paymentMapper::toDto).collect(Collectors.toList());
	}

	public Page<PaymentDTO> getAllPayments(Pageable pageable) {
		Page<Payment> paymentPage = paymentRepository.findAll(pageable);
		return paymentPage.map(paymentMapper::toDto);
	}

	public Optional<PaymentDTO> getPaymentById(Integer id) {
		Optional<Payment> optionalPayment = paymentRepository.findById(id);
		return optionalPayment.map(paymentMapper::toDto);
	}

	public PaymentDTO createPayment(PaymentDTO paymentDTO) {
		Payment payment = paymentMapper.toEntity(paymentDTO);
		payment = paymentRepository.save(payment);
		return paymentMapper.toDto(payment);
	}

	public PaymentDTO updatePayment(Integer id, PaymentDTO paymentDTO) {
		Payment existingPayment = paymentRepository.findById(id).orElse(null);
		if (existingPayment == null) {
			return null; // or throw exception
		}
		Payment updatedPayment = paymentMapper.toEntity(paymentDTO);
		updatedPayment.setId(id);
		updatedPayment = paymentRepository.save(updatedPayment);
		return paymentMapper.toDto(updatedPayment);
	}

	public void deletePayment(Integer id) {
		paymentRepository.deleteById(id);
	}
}
