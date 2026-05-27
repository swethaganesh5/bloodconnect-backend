package com.bloodconnect.bloodconnect.service;

import com.bloodconnect.bloodconnect.model.Donor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private static final Map<String, List<String>>
        COMPATIBLE_GROUPS = new HashMap<>();

    static {
        COMPATIBLE_GROUPS.put("A+",
            Arrays.asList("A+", "A-", "O+", "O-"));
        COMPATIBLE_GROUPS.put("A-",
            Arrays.asList("A-", "O-"));
        COMPATIBLE_GROUPS.put("B+",
            Arrays.asList("B+", "B-", "O+", "O-"));
        COMPATIBLE_GROUPS.put("B-",
            Arrays.asList("B-", "O-"));
        COMPATIBLE_GROUPS.put("AB+",
            Arrays.asList(
                "A+","A-","B+","B-",
                "O+","O-","AB+","AB-"));
        COMPATIBLE_GROUPS.put("AB-",
            Arrays.asList("A-","B-","O-","AB-"));
        COMPATIBLE_GROUPS.put("O+",
            Arrays.asList("O+", "O-"));
        COMPATIBLE_GROUPS.put("O-",
            Arrays.asList("O-"));
    }

    private static final List<String> RARE_GROUPS =
        Arrays.asList("AB-", "O-", "B-", "A-");

    public List<Map<String, Object>> findMatchingDonors(
            List<Donor> allDonors,
            String requestedBloodGroup,
            double hospitalLat,
            double hospitalLon) {

        List<String> compatibleGroups =
            COMPATIBLE_GROUPS.getOrDefault(
                requestedBloodGroup, new ArrayList<>());

        boolean isRare = RARE_GROUPS.contains(
            requestedBloodGroup);
        double maxDistanceKm = isRare ? 50.0 : 15.0;

        List<Map<String, Object>> matchedDonors =
            new ArrayList<>();

        for (Donor donor : allDonors) {
            if (!compatibleGroups.contains(
                    donor.getBloodGroup())) continue;
            if (!Boolean.TRUE.equals(
                    donor.getAvailable())) continue;
            if (donor.getLatitude() == null
                    || donor.getLongitude() == null)
                continue;
            if (!isEligible(donor)) continue;
            if (!isAvailableNow(donor)) continue;

            double distance = calculateDistance(
                hospitalLat, hospitalLon,
                donor.getLatitude(),
                donor.getLongitude());

            if (distance > maxDistanceKm) {
                if (!Boolean.TRUE.equals(
                        donor.getWillingToTravel()))
                    continue;
                if (distance > 50.0) continue;
            }

            Map<String, Object> donorMap =
                new HashMap<>();
            donorMap.put("id", donor.getId());
            donorMap.put("name", donor.getName());
            donorMap.put("email", donor.getEmail());
            donorMap.put("phone", donor.getPhone());
            donorMap.put("bloodGroup",
                donor.getBloodGroup());
            donorMap.put("distanceKm",
                Math.round(distance * 10.0) / 10.0);
            donorMap.put("donationCount",
                donor.getDonationCount());
            matchedDonors.add(donorMap);
        }

        matchedDonors.sort((a, b) ->
            Double.compare(
                (Double) a.get("distanceKm"),
                (Double) b.get("distanceKm")));

        return matchedDonors.stream()
            .limit(5)
            .collect(Collectors.toList());
    }

    public double calculateDistance(
            double lat1, double lon1,
            double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a =
            Math.sin(dLat/2) * Math.sin(dLat/2)
            + Math.cos(Math.toRadians(lat1))
            * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(
            Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    private boolean isEligible(Donor donor) {
        if (Boolean.TRUE.equals(
                donor.getHasRecentSurgery()))
            return false;
        if (donor.getWeightKg() != null
                && donor.getWeightKg() < 50)
            return false;
        if (donor.getLastDonationDate() != null) {
            long daysSince = java.time.temporal
                .ChronoUnit.DAYS.between(
                    donor.getLastDonationDate(),
                    java.time.LocalDateTime.now());
            if (daysSince < 90) return false;
        }
        return true;
    }

    private boolean isAvailableNow(Donor donor) {
        if (donor.getAvailableDays() == null
                || donor.getAvailableDays().isEmpty())
            return true;
        String today = LocalDate.now()
            .getDayOfWeek().name();
        today = today.substring(0,1)
            + today.substring(1).toLowerCase();
        if (!donor.getAvailableDays().contains(today))
            return false;
        if (donor.getAvailableTimeStart() != null
                && donor.getAvailableTimeEnd() != null) {
            LocalTime now = LocalTime.now();
            LocalTime start = LocalTime.parse(
                donor.getAvailableTimeStart());
            LocalTime end = LocalTime.parse(
                donor.getAvailableTimeEnd());
            return now.isAfter(start)
                && now.isBefore(end);
        }
        return true;
    }
}