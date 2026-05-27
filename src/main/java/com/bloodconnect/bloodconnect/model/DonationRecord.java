package com.bloodconnect.bloodconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "donation_records")
@Data
public class DonationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long donorId;
    private Long requestId;
    private String hospital;
    private LocalDateTime donationDate;

    private Boolean reminder1Sent = false;
    private Boolean reminder3Sent = false;
    private Boolean reminder7Sent = false;
    private Boolean reminder90Sent = false;

    @PrePersist
    protected void onCreate() {
        donationDate = LocalDateTime.now();
    }
}