package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new RuntimeException("Username is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (user.getRole() == null) {
            user.setRole(Role.STUDENT);
        }

        if ((user.getDepartmentIds() == null || user.getDepartmentIds().isEmpty()) && user.getDepartmentId() != null) {
            List<String> departments = new ArrayList<>();
            departments.add(user.getDepartmentId());
            user.setDepartmentIds(departments);
        }

        return userRepository.save(user);
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    public User changePassword(String username, String currentPassword, String newPassword) {
        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username is required");
        }
        if (currentPassword == null || currentPassword.isBlank()) {
            throw new RuntimeException("Current password is required");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new RuntimeException("New password is required");
        }
        if (newPassword.length() < 6) {
            throw new RuntimeException("New password must be at least 6 characters");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(currentPassword)) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String getDashboard(User user) {
        switch (user.getRole()) {
            case ADMIN:
                return "Welcome Admin - Full Access";
            case HOD:
                return "Welcome HOD - Department Access";
            case FACULTY:
                return "Welcome Faculty - Limited Access";
            case STUDENT:
                return "Welcome Student - Basic Access";
            default:
                return "Invalid Role";
        }
    }
}
