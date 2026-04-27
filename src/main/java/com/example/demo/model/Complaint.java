package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long complaintId;
    private String category;
    private String faculty;
    private Integer year;
    private String dept;
    private String course;
    private String courseCode;
    private String subjectId;
    private String name;
    private String studentId;
    private String hostel;
    private String sport;

    @Column(length = 4000)
    private String text;

    private String date;
    private String submittedBy;
    private Boolean plagged;

    private String recipientType;
    private String targetDepartmentId;
    private String targetHodUsername;
    private String targetFacultyUsername;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId", referencedColumnName = "id", insertable = false, updatable = false)
    private Subject subject;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submittedBy", referencedColumnName = "username", insertable = false, updatable = false)
    private User submittedByUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetHodUsername", referencedColumnName = "username", insertable = false, updatable = false)
    private User targetHodUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetFacultyUsername", referencedColumnName = "username", insertable = false, updatable = false)
    private User targetFacultyUser;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getComplaintId() { return complaintId; }
    public void setComplaintId(Long complaintId) { this.complaintId = complaintId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getHostel() { return hostel; }
    public void setHostel(String hostel) { this.hostel = hostel; }

    public String getSport() { return sport; }
    public void setSport(String sport) { this.sport = sport; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }

    public Boolean getPlagged() { return plagged; }
    public void setPlagged(Boolean plagged) { this.plagged = plagged; }

    public String getRecipientType() { return recipientType; }
    public void setRecipientType(String recipientType) { this.recipientType = recipientType; }

    public String getTargetDepartmentId() { return targetDepartmentId; }
    public void setTargetDepartmentId(String targetDepartmentId) { this.targetDepartmentId = targetDepartmentId; }

    public String getTargetHodUsername() { return targetHodUsername; }
    public void setTargetHodUsername(String targetHodUsername) { this.targetHodUsername = targetHodUsername; }

    public String getTargetFacultyUsername() { return targetFacultyUsername; }
    public void setTargetFacultyUsername(String targetFacultyUsername) { this.targetFacultyUsername = targetFacultyUsername; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public User getSubmittedByUser() { return submittedByUser; }
    public void setSubmittedByUser(User submittedByUser) { this.submittedByUser = submittedByUser; }

    public User getTargetHodUser() { return targetHodUser; }
    public void setTargetHodUser(User targetHodUser) { this.targetHodUser = targetHodUser; }

    public User getTargetFacultyUser() { return targetFacultyUser; }
    public void setTargetFacultyUser(User targetFacultyUser) { this.targetFacultyUser = targetFacultyUser; }
}
