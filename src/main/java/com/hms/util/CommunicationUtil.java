package com.hms.util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Component
public class CommunicationUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendAppointmentReminder(String patientEmail, String appointmentDetails) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(patientEmail);
            helper.setSubject("Appointment Reminder");
            helper.setText("Dear Patient,\n\nThis is a reminder for your upcoming appointment:\n\n" + appointmentDetails + "\n\nBest regards,\n Clinic");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.getMessage();
           
        }
    }
}
