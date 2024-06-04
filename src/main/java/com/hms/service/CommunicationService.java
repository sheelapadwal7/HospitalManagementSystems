package com.hms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.util.CommunicationUtil;

@Service
public class CommunicationService {

    @Autowired
    private CommunicationUtil communicationUtil;

    public void sendAppointmentReminder(String patientEmail, String appointmentDetails) {
        communicationUtil.sendAppointmentReminder(patientEmail, appointmentDetails);
    }
}
