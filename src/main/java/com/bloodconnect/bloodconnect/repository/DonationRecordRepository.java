package com.bloodconnect.bloodconnect.repository;

import com.bloodconnect.bloodconnect.model.DonationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRecordRepository
        extends JpaRepository<DonationRecord, Long> {

    List<DonationRecord> findByDonorId(Long donorId);

    List<DonationRecord> findByReminder1SentFalse();
    List<DonationRecord> findByReminder3SentFalse();
    List<DonationRecord> findByReminder7SentFalse();
    List<DonationRecord> findByReminder90SentFalse();
}