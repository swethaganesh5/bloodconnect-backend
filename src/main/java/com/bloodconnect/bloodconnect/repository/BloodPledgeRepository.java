package com.bloodconnect.bloodconnect.repository;

import com.bloodconnect.bloodconnect.model.BloodPledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BloodPledgeRepository
        extends JpaRepository<BloodPledge, Long> {

    List<BloodPledge> findByFulfilledFalse();
}