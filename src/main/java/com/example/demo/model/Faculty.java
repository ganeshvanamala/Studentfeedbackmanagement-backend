package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "faculty")
public class Faculty {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    private String id;

    private String name;
    private String employeeId;
    private String branch;
    private String departmentId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId", referencedColumnName = "employeeId", insertable = false, updatable = false)
    private User user;

    @JsonIgnore
    @Lob
    @Column(columnDefinition = "TEXT")
    private String teachingJson;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Map<String, Object>> getTeaching() {
        if (teachingJson == null || teachingJson.isBlank()) return new ArrayList<>();
        try {
            return OBJECT_MAPPER.readValue(teachingJson, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    public void setTeaching(List<Map<String, Object>> teaching) {
        try {
            this.teachingJson = OBJECT_MAPPER.writeValueAsString(teaching == null ? Collections.emptyList() : teaching);
        } catch (Exception ex) {
            this.teachingJson = "[]";
        }
    }
}
