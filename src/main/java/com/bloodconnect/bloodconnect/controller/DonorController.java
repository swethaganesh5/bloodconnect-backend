package com.bloodconnect.bloodconnect.controller;

import com.bloodconnect.bloodconnect.model.Donor;
import com.bloodconnect.bloodconnect.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "*")
public class DonorController {

    @Autowired
    private DonorRepository donorRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerDonor(
            @RequestBody Donor donor) {
        try {
            if (donorRepository.existsByEmail(
                    donor.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error",
                        "Email already registered"));
            }
            Donor saved =
                donorRepository.save(donor);
            return ResponseEntity.ok(Map.of(
                "message", "Registered successfully!",
                "id", saved.getId()));
        } catch (Exception e) {
            return ResponseEntity
                .internalServerError()
                .body(Map.of("error",
                    e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDonors() {
        return ResponseEntity.ok(
            donorRepository.findAll());
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard() {
        return ResponseEntity.ok(
            donorRepository.findTopDonors());
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<?> updateAvailability(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        return donorRepository.findById(id)
            .map(donor -> {
                donor.setAvailable(
                    body.get("available"));
                donorRepository.save(donor);
                return ResponseEntity.ok(
                    Map.of("message", "Updated!"));
            })
            .orElse(ResponseEntity
                .notFound().build());
    }
}