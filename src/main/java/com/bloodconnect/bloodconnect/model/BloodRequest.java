package com.bloodconnect.bloodconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "blood_requests")
@Data
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;
    private String phone;
    private String bloodGroup;
    private String hospital;
    private Double latitude;
    private Double longitude;
    private Boolean isPreSurgery = false;
    private LocalDateTime surgeryDate;
    private Integer unitsNeeded = 1;
    private String status = "PENDING";
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}