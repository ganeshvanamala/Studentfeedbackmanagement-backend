package com.example.demo.model;

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
}
