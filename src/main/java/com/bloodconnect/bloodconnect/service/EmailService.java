package com.bloodconnect.bloodconnect.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    private void sendEmail(String toEmail, String toName, String subject, String html) {
        try {
            String body = """
                {
                    "sender": {"name": "BloodConnect", "email": "swethaworks9@gmail.com"},
                    "to": [{"email": "%s", "name": "%s"}],
                    "subject": "%s",
                    "htmlContent": "%s"
                }
                """.formatted(toEmail, toName, subject, html.replace("\"", "\\\"").replace("\n", ""));

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                .header("accept", "application/json")
                .header("api-key", apiKey)
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Email sent! Status: " + response.statusCode());
        } catch (Exception e) {
            System.err.println("Email failed: " + e.getMessage());
        }
    }

    public void sendBloodRequestEmail(String donorEmail, String donorName, String bloodGroup, String hospital, String patientPhone) {
        String subject = "URGENT - Blood Needed: " + bloodGroup + " at " + hospital;
        String html = "<div style='font-family:Arial'><h2 style='color:#e53935'>BloodConnect - Urgent Request</h2><p>Dear <strong>" + donorName + "</strong>,</p><p>Someone urgently needs your help!</p><p><strong>Blood Group:</strong> " + bloodGroup + "</p><p><strong>Hospital:</strong> " + hospital + "</p><p><strong>Patient Contact:</strong> " + patientPhone + "</p><p>Please call the patient immediately!</p><p>BloodConnect Team</p></div>";
        sendEmail(donorEmail, donorName, subject, html);
    }

    public void sendPostDonationReminder(String donorEmail, String donorName, int dayNumber) {
        String subject = "BloodConnect - Day " + dayNumber + " tip";
        String html = "<p>Dear " + donorName + ", thank you for donating! Day " + dayNumber + " reminder from BloodConnect.</p>";
        sendEmail(donorEmail, donorName, subject, html);
    }

    public void sendPledgeReminder(String email, String name) {
        String subject = "BloodConnect - Fulfill your pledge!";
        String html = "<p>Dear " + name + ", you pledged to donate blood. You are now eligible!</p>";
        sendEmail(email, name, subject, html);
    }
}