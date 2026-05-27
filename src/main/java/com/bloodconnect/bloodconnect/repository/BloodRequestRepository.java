package com.bloodconnect.bloodconnect.repository;

import com.bloodconnect.bloodconnect.model.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BloodRequestRepository
        extends JpaRepository<BloodRequest, Long> {

    List<BloodRequest> findByStatus(String status);
}