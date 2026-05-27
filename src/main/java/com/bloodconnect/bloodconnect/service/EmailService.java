package com.bloodconnect.bloodconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendBloodRequestEmail(
            String donorEmail,
            String donorName,
            String bloodGroup,
            String hospital,
            String patientPhone) {
        try {
            MimeMessage message =
                mailSender.createMimeMessage();
            MimeMessageHelper helper =
                new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(donorEmail);
            helper.setSubject(
                "URGENT - Blood Needed: "
                + bloodGroup + " at " + hospital);
            String html =
                "<div style='font-family:Arial;"
                + "max-width:600px'>"
                + "<div style='background:#e53935;"
                + "padding:20px;border-radius:8px"
                + " 8px 0 0'>"
                + "<h2 style='color:white;margin:0'>"
                + "BloodConnect - Urgent Request"
                + "</h2></div>"
                + "<div style='padding:20px;"
                + "border:1px solid #eee'>"
                + "<p>Dear <strong>"
                + donorName + "</strong>,</p>"
                + "<p>Someone urgently needs "
                + "your help!</p>"
                + "<table style='width:100%'>"
                + "<tr><td><strong>Blood Group"
                + "</strong></td><td><strong"
                + " style='color:#e53935'>"
                + bloodGroup + "</strong></td></tr>"
                + "<tr><td>Hospital</td><td>"
                + hospital + "</td></tr>"
                + "<tr><td>Patient Contact</td><td>"
                + patientPhone + "</td></tr>"
                + "</table>"
                + "<p>Please call the patient "
                + "immediately if you can donate.</p>"
                + "<p style='color:#999'>"
                + "BloodConnect Team</p>"
                + "</div></div>";
            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println(
                "Email failed: " + e.getMessage());
        }
    }

    public void sendPostDonationReminder(
            String donorEmail,
            String donorName,
            int dayNumber) {
        try {
            SimpleMailMessage message =
                new SimpleMailMessage();
            message.setTo(donorEmail);
            if (dayNumber == 1) {
                message.setSubject(
                    "Thank you for donating! "
                    + "Day 1 care tips");
                message.setText(
                    "Dear " + donorName + ",\n\n"
                    + "Thank you for saving a life!\n"
                    + "Day 1 tip: Drink 3 extra "
                    + "glasses of water today.\n\n"
                    + "BloodConnect Team");
            } else if (dayNumber == 3) {
                message.setSubject(
                    "BloodConnect - Day 3 tip");
                message.setText(
                    "Dear " + donorName + ",\n\n"
                    + "Day 3 tip: Eat iron-rich foods"
                    + " - spinach, dates, eggs.\n\n"
                    + "BloodConnect Team");
            } else if (dayNumber == 7) {
                message.setSubject(
                    "BloodConnect - Day 7 update");
                message.setText(
                    "Dear " + donorName + ",\n\n"
                    + "Day 7: Light exercise is "
                    + "safe now!\n\n"
                    + "BloodConnect Team");
            } else if (dayNumber == 90) {
                message.setSubject(
                    "You can donate blood again!");
                message.setText(
                    "Dear " + donorName + ",\n\n"
                    + "90 days passed! You are "
                    + "eligible to donate again!\n\n"
                    + "BloodConnect Team");
            }
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println(
                "Reminder failed: " + e.getMessage());
        }
    }

    public void sendPledgeReminder(
            String email, String name) {
        try {
            SimpleMailMessage message =
                new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(
                "BloodConnect - Fulfill your pledge!");
            message.setText(
                "Dear " + name + ",\n\n"
                + "3 months ago you pledged to "
                + "donate blood after recovery.\n"
                + "You are now eligible!\n\n"
                + "BloodConnect Team");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println(
                "Pledge reminder failed: "
                + e.getMessage());
        }
    }
}