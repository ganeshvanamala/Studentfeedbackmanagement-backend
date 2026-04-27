package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    private String id;

    private String name;
    private String code;
    private String branch;
    private String departmentId;
    private Integer year;

    @JsonIgnore
    @OneToMany(mappedBy = "targetSubject", fetch = FetchType.LAZY)
    private List<Form> targetedForms = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Response> responses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Complaint> complaints = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public List<Form> getTargetedForms() { return targetedForms == null ? new ArrayList<>() : targetedForms; }
    public void setTargetedForms(List<Form> targetedForms) { this.targetedForms = targetedForms == null ? new ArrayList<>() : targetedForms; }

    public List<Response> getResponses() { return responses == null ? new ArrayList<>() : responses; }
    public void setResponses(List<Response> responses) { this.responses = responses == null ? new ArrayList<>() : responses; }

    public List<Complaint> getComplaints() { return complaints == null ? new ArrayList<>() : complaints; }
    public void setComplaints(List<Complaint> complaints) { this.complaints = complaints == null ? new ArrayList<>() : complaints; }
}
