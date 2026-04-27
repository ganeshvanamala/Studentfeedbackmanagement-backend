package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "form")
public class Form {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<List<Map<String, Object>>> LIST_OF_MAP_TYPE = new TypeReference<>() {};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;

    private String createdByUserId;
    private String createdByRole;

    private Boolean sendToAll;

    private String targetSubjectId;
    private Integer targetYear;
    private String departmentId;

    private String scopeType;

    private String targetSubjectCode;
    private String targetSubjectName;
    private String targetBranch;

    private String createdAt;

    @ElementCollection
    private List<String> departmentIds = new ArrayList<>();

    @ElementCollection
    private List<String> scopeIds = new ArrayList<>();

    @JsonIgnore
    @Lob
    @Column(name = "questions_json", columnDefinition = "TEXT")
    private String questionsJson;

    @JsonIgnore
    @Lob
    @Column(name = "responses_json", columnDefinition = "TEXT")
    private String responsesJson;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdByUserId", referencedColumnName = "username", insertable = false, updatable = false)
    private User createdByUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetSubjectId", referencedColumnName = "id", insertable = false, updatable = false)
    private Subject targetSubject;

    @JsonIgnore
    @OneToMany(mappedBy = "form", fetch = FetchType.LAZY)
    private List<Question> questionsList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "form", fetch = FetchType.LAZY)
    private List<Response> submittedResponses = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(String createdByUserId) { this.createdByUserId = createdByUserId; }

    public String getCreatedByRole() { return createdByRole; }
    public void setCreatedByRole(String createdByRole) { this.createdByRole = createdByRole; }

    public Boolean getSendToAll() { return sendToAll; }
    public void setSendToAll(Boolean sendToAll) { this.sendToAll = sendToAll; }

    public String getTargetSubjectId() { return targetSubjectId; }
    public void setTargetSubjectId(String targetSubjectId) { this.targetSubjectId = targetSubjectId; }

    public Integer getTargetYear() { return targetYear; }
    public void setTargetYear(Integer targetYear) { this.targetYear = targetYear; }

    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }

    public String getScopeType() { return scopeType; }
    public void setScopeType(String scopeType) { this.scopeType = scopeType; }

    public String getTargetSubjectCode() { return targetSubjectCode; }
    public void setTargetSubjectCode(String targetSubjectCode) { this.targetSubjectCode = targetSubjectCode; }

    public String getTargetSubjectName() { return targetSubjectName; }
    public void setTargetSubjectName(String targetSubjectName) { this.targetSubjectName = targetSubjectName; }

    public String getTargetBranch() { return targetBranch; }
    public void setTargetBranch(String targetBranch) { this.targetBranch = targetBranch; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public User getCreatedByUser() { return createdByUser; }
    public void setCreatedByUser(User createdByUser) { this.createdByUser = createdByUser; }

    public Subject getTargetSubject() { return targetSubject; }
    public void setTargetSubject(Subject targetSubject) { this.targetSubject = targetSubject; }

    public List<Question> getQuestionsList() { return questionsList == null ? new ArrayList<>() : questionsList; }
    public void setQuestionsList(List<Question> questionsList) { this.questionsList = questionsList == null ? new ArrayList<>() : questionsList; }

    public List<Response> getSubmittedResponses() { return submittedResponses == null ? new ArrayList<>() : submittedResponses; }
    public void setSubmittedResponses(List<Response> submittedResponses) { this.submittedResponses = submittedResponses == null ? new ArrayList<>() : submittedResponses; }

    public List<String> getDepartmentIds() {
        return departmentIds == null ? new ArrayList<>() : departmentIds;
    }

    public void setDepartmentIds(List<String> departmentIds) {
        this.departmentIds = departmentIds == null ? new ArrayList<>() : departmentIds;
    }

    public List<String> getScopeIds() {
        return scopeIds == null ? new ArrayList<>() : scopeIds;
    }

    public void setScopeIds(List<String> scopeIds) {
        this.scopeIds = scopeIds == null ? new ArrayList<>() : scopeIds;
    }

    public List<Map<String, Object>> getQuestions() {
        if (questionsJson == null || questionsJson.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(questionsJson, LIST_OF_MAP_TYPE);
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    public void setQuestions(List<Map<String, Object>> questions) {
        try {
            this.questionsJson = OBJECT_MAPPER.writeValueAsString(
                    questions == null ? Collections.emptyList() : questions
            );
        } catch (Exception ex) {
            this.questionsJson = "[]";
        }
    }

    public List<Map<String, Object>> getResponses() {
        if (responsesJson == null || responsesJson.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(responsesJson, LIST_OF_MAP_TYPE);
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    public void setResponses(List<Map<String, Object>> responses) {
        try {
            this.responsesJson = OBJECT_MAPPER.writeValueAsString(
                    responses == null ? Collections.emptyList() : responses
            );
        } catch (Exception ex) {
            this.responsesJson = "[]";
        }
    }
}
