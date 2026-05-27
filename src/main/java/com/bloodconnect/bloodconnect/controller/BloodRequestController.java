package com.bloodconnect.bloodconnect.controller;

import com.bloodconnect.bloodconnect.model.*;
import com.bloodconnect.bloodconnect.repository.*;
import com.bloodconnect.bloodconnect.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class BloodRequestController {

    @Autowired
    private BloodRequestRepository requestRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private DonationRecordRepository donationRecordRepo;

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/create")
    public ResponseEntity<?> createRequest(
            @RequestBody BloodRequest request) {
        try {
            BloodRequest saved =
                requestRepository.save(request);

            List<Donor> allDonors =
                donorRepository.findAll();

            List<Map<String, Object>> matched =
                matchingService.findMatchingDonors(
                    allDonors,
                    request.getBloodGroup(),
                    request.getLatitude(),
                    request.getLongitude());

            int emailsSent = 0;
            for (Map<String, Object> donor : matched) {
                emailService.sendBloodRequestEmail(
                    (String) donor.get("email"),
                    (String) donor.get("name"),
                    request.getBloodGroup(),
                    request.getHospital(),
                    request.getPhone());
                emailsSent++;
            }

            return ResponseEntity.ok(Map.of(
                "message", "Request created!",
                "requestId", saved.getId(),
                "matchedDonors", matched,
                "emailsSent", emailsSent));

        } catch (Exception e) {
            return ResponseEntity
                .internalServerError()
                .body(Map.of("error",
                    e.getMessage()));
        }
    }

    @PostMapping("/confirm-donation")
    public ResponseEntity<?> confirmDonation(
            @RequestBody Map<String, Long> body) {
        try {
            Long donorId = body.get("donorId");
            Long requestId = body.get("requestId");

            donorRepository.findById(donorId)
                .ifPresent(donor -> {
                    donor.setDonationCount(
                        donor.getDonationCount() + 1);
                    donorRepository.save(donor);
                });

            DonationRecord record =
                new DonationRecord();
            record.setDonorId(donorId);
            record.setRequestId(requestId);
            donationRecordRepo.save(record);

            requestRepository.findById(requestId)
                .ifPresent(req -> {
                    req.setStatus("FULFILLED");
                    requestRepository.save(req);
                });

            return ResponseEntity.ok(Map.of(
                "message",
                "Donation confirmed!"));

        } catch (Exception e) {
            return ResponseEntity
                .internalServerError()
                .body(Map.of("error",
                    e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(
            requestRepository.findAll());
    }
}