package com.hms.service;

import com.hms.dto.PrescriptionDTO;
import com.hms.mapper.PrescriptionMapper;
import com.hms.model.Prescription;
import com.hms.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    @Autowired
    PrescriptionRepository prescriptionRepository;
    @Autowired
    PrescriptionMapper prescriptionMapper;

    public Page<PrescriptionDTO> getAllPrescriptions(Pageable pageable) {
        return prescriptionRepository.findAll(pageable).map(prescriptionMapper::toDto);
    }

    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);
        prescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toDto(prescription);
    }

    public List<PrescriptionDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(prescriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    public PrescriptionDTO getPrescriptionById(Integer id) {
        Optional<Prescription> optionalPrescription = prescriptionRepository.findById(id);
        return optionalPrescription.map(prescriptionMapper::toDto).orElse(null);
    }

    public PrescriptionDTO updatePrescription(Integer id, PrescriptionDTO prescriptionDTO) {
        Prescription existingPrescription = prescriptionRepository.findById(id).orElse(null);
        if (existingPrescription == null) {
            return null; // or throw exception
        }
        Prescription updatedPrescription = prescriptionMapper.toEntity(prescriptionDTO);
        updatedPrescription.setPrescriptionId(id);
        updatedPrescription = prescriptionRepository.save(updatedPrescription);
        return prescriptionMapper.toDto(updatedPrescription);
    }

    public void deletePrescription(Integer id) {
        prescriptionRepository.deleteById(id);
    }
}
