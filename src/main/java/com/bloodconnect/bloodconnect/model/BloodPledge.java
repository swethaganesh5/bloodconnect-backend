package com.bloodconnect.bloodconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "blood_pledges")
@Data
public class BloodPledge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;
    private String email;
    private String phone;
    private String bloodGroup;
    private Boolean fulfilled = false;
    private LocalDateTime pledgeDate;
    private LocalDateTime reminderSentAt;

    @PrePersist
    protected void onCreate() {
        pledgeDate = LocalDateTime.now();
    }
}