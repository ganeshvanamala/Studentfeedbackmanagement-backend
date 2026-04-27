package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String departmentId;

    @ElementCollection
    private List<String> departmentIds = new ArrayList<>();

    @ElementCollection
    private List<String> subjectIds = new ArrayList<>();

    private String fullName;
    private String employeeId;
    private String email;
    private String studentId;
    private Integer year;

    private String createdByUserId;
    private String createdByRole;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdByUserId", referencedColumnName = "username", insertable = false, updatable = false)
    private User createdByUser;

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser", fetch = FetchType.LAZY)
    private List<User> createdUsers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "createdByUser", fetch = FetchType.LAZY)
    private List<Form> createdForms = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "submittedByUser", fetch = FetchType.LAZY)
    private List<Response> submittedResponses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "submittedByUser", fetch = FetchType.LAZY)
    private List<Complaint> submittedComplaints = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "targetUserAccount", fetch = FetchType.LAZY)
    private List<StudentReply> targetedReplies = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "targetHodUser", fetch = FetchType.LAZY)
    private List<Complaint> hodComplaints = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "targetFacultyUser", fetch = FetchType.LAZY)
    private List<Complaint> facultyComplaints = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    @JsonProperty("role")
    public void setRoleFromString(String role) {
        if (role == null || role.isBlank()) {
            this.role = null;
            return;
        }
        this.role = Role.valueOf(role.trim().toUpperCase());
    }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public List<String> getDepartmentIds() { return departmentIds == null ? new ArrayList<>() : departmentIds; }
    public void setDepartmentIds(List<String> departmentIds) { this.departmentIds = departmentIds == null ? new ArrayList<>() : departmentIds; }

    public List<String> getSubjectIds() { return subjectIds == null ? new ArrayList<>() : subjectIds; }
    public void setSubjectIds(List<String> subjectIds) { this.subjectIds = subjectIds == null ? new ArrayList<>() : subjectIds; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(String createdByUserId) { this.createdByUserId = createdByUserId; }

    public String getCreatedByRole() { return createdByRole; }
    public void setCreatedByRole(String createdByRole) { this.createdByRole = createdByRole; }

    public User getCreatedByUser() { return createdByUser; }
    public void setCreatedByUser(User createdByUser) { this.createdByUser = createdByUser; }

    public List<User> getCreatedUsers() { return createdUsers == null ? new ArrayList<>() : createdUsers; }
    public void setCreatedUsers(List<User> createdUsers) { this.createdUsers = createdUsers == null ? new ArrayList<>() : createdUsers; }

    public List<Form> getCreatedForms() { return createdForms == null ? new ArrayList<>() : createdForms; }
    public void setCreatedForms(List<Form> createdForms) { this.createdForms = createdForms == null ? new ArrayList<>() : createdForms; }

    public List<Response> getSubmittedResponses() { return submittedResponses == null ? new ArrayList<>() : submittedResponses; }
    public void setSubmittedResponses(List<Response> submittedResponses) { this.submittedResponses = submittedResponses == null ? new ArrayList<>() : submittedResponses; }

    public List<Complaint> getSubmittedComplaints() { return submittedComplaints == null ? new ArrayList<>() : submittedComplaints; }
    public void setSubmittedComplaints(List<Complaint> submittedComplaints) { this.submittedComplaints = submittedComplaints == null ? new ArrayList<>() : submittedComplaints; }

    public List<StudentReply> getTargetedReplies() { return targetedReplies == null ? new ArrayList<>() : targetedReplies; }
    public void setTargetedReplies(List<StudentReply> targetedReplies) { this.targetedReplies = targetedReplies == null ? new ArrayList<>() : targetedReplies; }

    public List<Complaint> getHodComplaints() { return hodComplaints == null ? new ArrayList<>() : hodComplaints; }
    public void setHodComplaints(List<Complaint> hodComplaints) { this.hodComplaints = hodComplaints == null ? new ArrayList<>() : hodComplaints; }

    public List<Complaint> getFacultyComplaints() { return facultyComplaints == null ? new ArrayList<>() : facultyComplaints; }
    public void setFacultyComplaints(List<Complaint> facultyComplaints) { this.facultyComplaints = facultyComplaints == null ? new ArrayList<>() : facultyComplaints; }
}
