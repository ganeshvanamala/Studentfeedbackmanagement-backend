package com.example.demo.repository;

import com.example.demo.model.StudentReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentReplyRepository extends JpaRepository<StudentReply, Long> {
    List<StudentReply> findByTargetUserOrderByIdDesc(String targetUser);
}
