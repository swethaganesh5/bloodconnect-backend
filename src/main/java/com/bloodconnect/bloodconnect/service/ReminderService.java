package com.bloodconnect.bloodconnect.service;

import com.bloodconnect.bloodconnect.model.*;
import com.bloodconnect.bloodconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ReminderService {

    @Autowired
    private DonationRecordRepository donationRecordRepo;

    @Autowired
    private DonorRepository donorRepo;

    @Autowired
    private BloodPledgeRepository pledgeRepo;

    @Autowired
    private EmailService emailService;

    // Runs every day at 9am
    @Scheduled(cron = "0 0 9 * * *")
    public void sendPostDonationReminders() {
        List<DonationRecord> records =
            donationRecordRepo.findAll();
        for (DonationRecord record : records) {
            Optional<Donor> donorOpt =
                donorRepo.findById(record.getDonorId());
            if (donorOpt.isEmpty()) continue;
            Donor donor = donorOpt.get();
            long daysSince = ChronoUnit.DAYS.between(
                record.getDonationDate(),
                LocalDateTime.now());
            if (daysSince >= 1
                    && !record.getReminder1Sent()) {
                emailService.sendPostDonationReminder(
                    donor.getEmail(),
                    donor.getName(), 1);
                record.setReminder1Sent(true);
                donationRecordRepo.save(record);
            }
            if (daysSince >= 3
                    && !record.getReminder3Sent()) {
                emailService.sendPostDonationReminder(
                    donor.getEmail(),
                    donor.getName(), 3);
                record.setReminder3Sent(true);
                donationRecordRepo.save(record);
            }
            if (daysSince >= 7
                    && !record.getReminder7Sent()) {
                emailService.sendPostDonationReminder(
                    donor.getEmail(),
                    donor.getName(), 7);
                record.setReminder7Sent(true);
                donationRecordRepo.save(record);
            }
            if (daysSince >= 90
                    && !record.getReminder90Sent()) {
                emailService.sendPostDonationReminder(
                    donor.getEmail(),
                    donor.getName(), 90);
                record.setReminder90Sent(true);
                donationRecordRepo.save(record);
            }
        }
    }

    // Runs every day at 10am
    @Scheduled(cron = "0 0 10 * * *")
    public void sendPledgeReminders() {
        List<BloodPledge> pledges =
            pledgeRepo.findByFulfilledFalse();
        for (BloodPledge pledge : pledges) {
            long daysSince = ChronoUnit.DAYS.between(
                pledge.getPledgeDate(),
                LocalDateTime.now());
            if (daysSince >= 90
                    && pledge.getReminderSentAt()
                    == null) {
                emailService.sendPledgeReminder(
                    pledge.getEmail(),
                    pledge.getPatientName());
                pledge.setReminderSentAt(
                    LocalDateTime.now());
                pledgeRepo.save(pledge);
            }
        }
    }
}