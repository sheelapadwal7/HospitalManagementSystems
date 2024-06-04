package com.hms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.hms.model.Appointment;
import com.hms.model.Patient;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.PatientRepository;
import com.hms.dto.AppointmentDTO;
import com.hms.mapper.AppointmentMapper;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    AppointmentMapper appointmentMapper;
    
    
    @Autowired
    PatientRepository patientRepository;

    public Page<AppointmentDTO> getAllAppointments(Pageable pageable) {
        Page<Appointment> appointmentPage = appointmentRepository.findAll(pageable);
        return appointmentPage.map(appointmentMapper::toDto);
    }

    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream().map(appointmentMapper::toDto).collect(Collectors.toList());
    }

    public Optional<AppointmentDTO> getAppointmentById(Integer id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        return optionalAppointment.map(appointmentMapper::toDto);
    }

    public AppointmentDTO createAppointment(Integer patientId, AppointmentDTO appointmentDTO) {
        // Convert AppointmentDTO to Appointment entity
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);

        // Check if the patient exists
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        Patient patient;
        if (optionalPatient.isPresent()) {
            // If the patient exists, use the existing patient
            patient = optionalPatient.get();
        } else {
            // If the patient does not exist, create a new patient
            patient = new Patient();
            patient.setId(appointmentDTO.getPatientId());
            patient.setName(appointmentDTO.getPatientName());
            patient.setContactNumber(appointmentDTO.getContactNumber());
            
            // Set any other properties as needed
            // Then save the new patient
            patient = patientRepository.save(patient);
        }

        // Set the patient for the appointment
        appointment.setPatient(patient);

        // Save the appointment entity
        appointment = appointmentRepository.save(appointment);

        // Convert the saved appointment entity back to DTO and return
        return appointmentMapper.toDto(appointment);
    }



    public AppointmentDTO updateAppointment(Integer id, AppointmentDTO appointmentDTO) {
        // Retrieve the existing appointment from the repository
        Appointment existingAppointment = appointmentRepository.findById(id).orElse(null);
       

        // Validate the data in the appointmentDTO (optional)

        // Update individual fields of the existing appointment
        // Assuming patientName is retrieved from associated Patient object
       // existingAppointment.getPatient().setId(appointmentDTO.getId());
        existingAppointment.getPatient().setName(appointmentDTO.getPatientName());
        existingAppointment.setContactNumber(appointmentDTO.getContactNumber());
        existingAppointment.setDateTime(appointmentDTO.getDateTime());

        // Save the updated appointment
        existingAppointment = appointmentRepository.save(existingAppointment);

        // Map the updated appointment entity to DTO and return
        return appointmentMapper.toDto(existingAppointment);
    }

  
    public void deleteAppointment(Integer id) {
        appointmentRepository.deleteById(id);
    }
    
    
    public AppointmentDTO markAppointmentAsAttended(Integer appointmentId) {
        // Retrieve the appointment using its ID
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null) {
            // Update appointment status to "attended"
            appointment.setAttended(true);
            // Set the patientId for the updated appointment
            //appointment.getPatient().setId(patientId);
            appointmentRepository.save(appointment);

            // Convert the updated appointment to DTO
            AppointmentDTO updatedAppointmentDTO = appointmentMapper.toDto(appointment);
            return updatedAppointmentDTO;
        }
        return null;
    }


}
