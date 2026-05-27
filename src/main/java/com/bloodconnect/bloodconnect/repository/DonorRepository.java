package com.bloodconnect.bloodconnect.repository;

import com.bloodconnect.bloodconnect.model.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonorRepository 
        extends JpaRepository<Donor, Long> {

    List<Donor> findByAvailableTrue();

    boolean existsByEmail(String email);

    @Query("SELECT d FROM Donor d ORDER BY d.donationCount DESC")
    List<Donor> findTopDonors();
}