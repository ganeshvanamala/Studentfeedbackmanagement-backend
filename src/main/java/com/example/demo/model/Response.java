package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long formId;

    private String submittedBy;

    private String category;

    @Column(length = 2000)
    private String answersJson; // store answers as JSON string

    private Integer year;
    private String dept;
    private String subjectId;
    private String faculty;
}