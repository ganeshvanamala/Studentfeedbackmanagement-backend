package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.security.AuthTokenService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final AuthTokenService authTokenService;

    public UserController(UserService userService, AuthTokenService authTokenService) {
        this.userService = userService;
        this.authTokenService = authTokenService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody User user) {
        User authenticatedUser = userService.loginUser(user.getUsername(), user.getPassword());
        AuthTokenService.IssuedToken issuedToken = authTokenService.issueToken(authenticatedUser);
        return new AuthResponse(authenticatedUser, issuedToken.token(), issuedToken.expiresAt());
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authTokenService.revokeToken(authHeader.substring("Bearer ".length()).trim());
        }
        return Map.of("message", "Logged out successfully");
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/dashboard")
    public String dashboard(@RequestBody User user) {
        return userService.getDashboard(user);
    }

    @PostMapping("/password")
    public User changePassword(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");
        return userService.changePassword(username, currentPassword, newPassword);
    }
}
