package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    private static final Set<String> ALLOWED_DEPARTMENTS = Set.of("cse", "ece", "eee", "mech", "civil");

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final EmailService emailService;

    @Value("${google.client.id:}")
    private String googleClientId;

    public AuthController(UserRepository userRepository, OtpService otpService, EmailService emailService) {
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = normalize(request.get("email"));
        String roleInput = request.get("role");

        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(email);
        Role role = parseRole(roleInput);

        if (email.isBlank() || role == null || userOpt.isEmpty() || userOpt.get().getRole() != role) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid user or role"));
        }

        try {
            String otp = otpService.generateAndStoreOtp(email);
            emailService.sendOtpEmail(email, otp);
            return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to send OTP"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        String email = normalize(request.get("email"));
        String otp = request.getOrDefault("otp", "").trim();
        String newPassword = request.getOrDefault("newPassword", "");
        String roleInput = request.get("role");

        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "User not found"));
        }

        Role role = parseRole(roleInput);
        if (role == null || userOpt.get().getRole() != role) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid role"));
        }

        if (newPassword.length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Password must be at least 6 characters"));
        }

        OtpService.OtpStatus otpStatus = otpService.validateOtp(email, otp);
        if (otpStatus == OtpService.OtpStatus.EXPIRED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "OTP expired"));
        }
        if (otpStatus != OtpService.OtpStatus.VALID) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid OTP"));
        }

        User user = userOpt.get();
        user.setPassword(newPassword);
        userRepository.save(user);
        otpService.clearOtp(email); // OTP one-time use

        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        String token = request.getOrDefault("token", "").trim();
        if (token.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Token is required"));
        }

        try {
            GoogleIdTokenVerifier.Builder builder = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            );
            if (!googleClientId.isBlank()) {
                builder.setAudience(Collections.singletonList(googleClientId));
            }
            GoogleIdTokenVerifier verifier = builder.build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid Google token"));
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = normalize(payload.getEmail());
            String name = (String) payload.get("name");

            if (email.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid Google token"));
            }

            Optional<User> existing = userRepository.findByUsernameIgnoreCase(email);
            if (existing.isPresent()) {
                return ResponseEntity.ok(existing.get());
            }

            User user = new User();
            user.setUsername(email);
            user.setEmail(email);
            user.setPassword(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
            user.setRole(Role.STUDENT);
            user.setFullName(name == null || name.isBlank() ? email : name);
            user.setDepartmentIds(new ArrayList<>());
            user.setSubjectIds(new ArrayList<>());

            User saved = userRepository.save(user);
            return ResponseEntity.ok(saved);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid Google token"));
        }
    }

    @PostMapping({"/google-complete-profile", "/complete-profile"})
    public ResponseEntity<?> completeGoogleProfile(@RequestBody Map<String, String> request) {
        String email = normalize(request.get("email"));
        String username = normalize(request.get("username"));
        String userIdentifier = !email.isBlank() ? email : username;
        String fullName = request.getOrDefault("fullName", "").trim();
        String studentId = request.getOrDefault("studentId", "").trim();
        String departmentId = normalize(request.get("departmentId"));
        String yearRaw = request.getOrDefault("year", "").trim();

        if (userIdentifier.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "User not found"));
        }

        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(userIdentifier);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "User not found"));
        }

        User user = userOpt.get();
        if (user.getRole() != Role.STUDENT) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Only STUDENT profile can be completed here"));
        }

        String existingStudentId = user.getStudentId() == null ? "" : user.getStudentId().trim();
        String existingDepartmentId = normalize(
                user.getDepartmentId() != null
                        ? user.getDepartmentId()
                        : (user.getDepartmentIds().isEmpty() ? "" : user.getDepartmentIds().get(0))
        );
        Integer existingYear = user.getYear();
        String existingFullName = user.getFullName() == null ? "" : user.getFullName().trim();

        boolean missingStudentId = existingStudentId.isBlank();
        boolean missingDepartmentId = existingDepartmentId.isBlank();
        boolean missingYear = existingYear == null || existingYear < 1 || existingYear > 4;
        boolean missingFullName = existingFullName.isBlank();

        if (!missingStudentId && !studentId.isBlank() && !existingStudentId.equalsIgnoreCase(studentId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Student ID is already set and cannot be changed"));
        }

        if (!missingDepartmentId && !departmentId.isBlank() && !existingDepartmentId.equals(departmentId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Department is already set and cannot be changed"));
        }

        if (!missingYear && !yearRaw.isBlank()) {
            int incomingYear;
            try {
                incomingYear = Integer.parseInt(yearRaw);
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Invalid year"));
            }
            if (incomingYear != existingYear) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Year is already set and cannot be changed"));
            }
        }

        boolean hasChanges = false;

        if (missingStudentId) {
            if (studentId.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Student ID is required"));
            }
            if (!studentId.matches("^[A-Za-z0-9-]{3,30}$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Student ID must be 3-30 letters, numbers, or hyphen"));
            }

            Optional<User> byStudentId = userRepository.findByStudentIdIgnoreCase(studentId);
            if (byStudentId.isPresent() && !byStudentId.get().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Student ID already exists"));
            }
            user.setStudentId(studentId);
            hasChanges = true;
        }

        if (missingDepartmentId) {
            if (!ALLOWED_DEPARTMENTS.contains(departmentId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Invalid department"));
            }
            user.setDepartmentId(departmentId);
            user.setDepartmentIds(new ArrayList<>(List.of(departmentId)));
            hasChanges = true;
        }

        if (missingYear) {
            int year;
            try {
                year = Integer.parseInt(yearRaw);
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Invalid year"));
            }

            if (year < 1 || year > 4) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Invalid year"));
            }
            user.setYear(year);
            hasChanges = true;
        }

        if (missingFullName) {
            if (fullName.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Full name is required"));
            }
            user.setFullName(fullName);
            hasChanges = true;
        }

        if (!hasChanges) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Profile is already completed"));
        }

        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    private Role parseRole(String roleInput) {
        if (roleInput == null || roleInput.isBlank()) return null;
        try {
            return Role.valueOf(roleInput.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
