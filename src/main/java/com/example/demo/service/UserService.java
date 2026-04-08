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
            throw new IllegalArgumentException("Username is required");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
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
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username or Student ID is required");
        }

        User user = userRepository.findByUsernameOrStudentId(username.trim(), username.trim())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }

        return user;
    }

    public User changePassword(String username, String currentPassword, String newPassword) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (currentPassword == null || currentPassword.isBlank()) {
            throw new IllegalArgumentException("Current password is required");
        }
        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getPassword().equals(currentPassword)) {
            throw new IllegalArgumentException("Current password is incorrect");
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
