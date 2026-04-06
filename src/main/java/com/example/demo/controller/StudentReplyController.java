package com.example.demo.controller;

import com.example.demo.model.StudentReply;
import com.example.demo.service.StudentReplyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replies")
@CrossOrigin(origins = "*")
public class StudentReplyController {

    private final StudentReplyService studentReplyService;

    public StudentReplyController(StudentReplyService studentReplyService) {
        this.studentReplyService = studentReplyService;
    }

    @GetMapping
    public List<StudentReply> getAll(@RequestParam(required = false) String targetUser) {
        if (targetUser != null && !targetUser.isBlank()) {
            return studentReplyService.findByTargetUser(targetUser);
        }
        return studentReplyService.findAll();
    }

    @PostMapping
    public StudentReply create(@RequestBody StudentReply reply) {
        return studentReplyService.save(reply);
    }

    @PutMapping("/mark-read/{targetUser}")
    public List<StudentReply> markRead(@PathVariable String targetUser) {
        return studentReplyService.markAllRead(targetUser);
    }
}
