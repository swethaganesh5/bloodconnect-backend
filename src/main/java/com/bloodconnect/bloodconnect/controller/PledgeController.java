package com.bloodconnect.bloodconnect.controller;

import com.bloodconnect.bloodconnect.model.BloodPledge;
import com.bloodconnect.bloodconnect.repository
        .BloodPledgeRepository;
import org.springframework.beans.factory.annotation
        .Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/pledges")
@CrossOrigin(origins = "*")
public class PledgeController {

    @Autowired
    private BloodPledgeRepository pledgeRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createPledge(
            @RequestBody BloodPledge pledge) {
        BloodPledge saved =
            pledgeRepository.save(pledge);
        return ResponseEntity.ok(Map.of(
            "message",
            "Pledge recorded! We will remind "
            + "you in 90 days.",
            "id", saved.getId()));
    }

    @PutMapping("/{id}/fulfill")
    public ResponseEntity<?> fulfillPledge(
            @PathVariable Long id) {
        return pledgeRepository.findById(id)
            .map(pledge -> {
                pledge.setFulfilled(true);
                pledgeRepository.save(pledge);
                return ResponseEntity.ok(
                    Map.of("message",
                        "Pledge fulfilled!"));
            })
            .orElse(ResponseEntity
                .notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> getAllPledges() {
        return ResponseEntity.ok(
            pledgeRepository.findAll());
    }
}