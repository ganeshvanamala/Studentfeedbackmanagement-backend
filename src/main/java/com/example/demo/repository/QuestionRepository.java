package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Question;
// No need for @Repository annotation as JpaRepository already provides the necessary implementation
public interface QuestionRepository extends JpaRepository<Question, Long> {
}