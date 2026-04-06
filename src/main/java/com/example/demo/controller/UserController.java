package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.loginUser(user.getUsername(), user.getPassword());
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
