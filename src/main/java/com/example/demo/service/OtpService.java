package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private static final long OTP_EXPIRY_MILLIS = 5 * 60 * 1000;
    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateAndStoreOtp(String email) {
        String normalizedEmail = normalize(email);
        String otp = String.format("%06d", random.nextInt(1_000_000));
        long expiresAt = System.currentTimeMillis() + OTP_EXPIRY_MILLIS;
        otpStore.put(normalizedEmail, new OtpEntry(otp, expiresAt));

        // For testing/debugging as requested
        System.out.println("OTP for " + normalizedEmail + " = " + otp);
        return otp;
    }

    public OtpStatus validateOtp(String email, String otp) {
        String normalizedEmail = normalize(email);
        OtpEntry entry = otpStore.get(normalizedEmail);
        if (entry == null) {
            return OtpStatus.INVALID;
        }
        if (System.currentTimeMillis() > entry.expiresAt()) {
            otpStore.remove(normalizedEmail);
            return OtpStatus.EXPIRED;
        }
        if (!entry.otp().equals(otp)) {
            return OtpStatus.INVALID;
        }
        return OtpStatus.VALID;
    }

    public void clearOtp(String email) {
        otpStore.remove(normalize(email));
    }

    private String normalize(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    public enum OtpStatus {
        VALID,
        INVALID,
        EXPIRED
    }

    private record OtpEntry(String otp, long expiresAt) {
    }
}
