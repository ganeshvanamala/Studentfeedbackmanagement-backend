package com.example.demo.service;

import com.example.demo.model.StudentReply;
import com.example.demo.repository.StudentReplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentReplyService {

    private final StudentReplyRepository studentReplyRepository;

    public StudentReplyService(StudentReplyRepository studentReplyRepository) {
        this.studentReplyRepository = studentReplyRepository;
    }

    public StudentReply save(StudentReply reply) {
        if (reply.getIsRead() == null) {
            reply.setIsRead(false);
        }
        return studentReplyRepository.save(reply);
    }

    public List<StudentReply> findAll() {
        return studentReplyRepository.findAll();
    }

    public List<StudentReply> findByTargetUser(String targetUser) {
        return studentReplyRepository.findByTargetUserOrderByIdDesc(targetUser);
    }

    public List<StudentReply> markAllRead(String targetUser) {
        List<StudentReply> replies = studentReplyRepository.findByTargetUserOrderByIdDesc(targetUser);
        for (StudentReply reply : replies) {
            reply.setIsRead(true);
        }
        return studentReplyRepository.saveAll(replies);
    }
}
