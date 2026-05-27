package com.bloodconnect.bloodconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "donors")
@Data
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String bloodGroup;
    private Double latitude;
    private Double longitude;
    private Boolean available = true;
    private Integer donationCount = 0;
    private String availableDays;
    private String availableTimeStart;
    private String availableTimeEnd;
    private Boolean willingToTravel = false;
    private Double weightKg;
    private Boolean hasRecentSurgery = false;
    private Boolean hasHighBP = false;
    private LocalDateTime lastDonationDate;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}