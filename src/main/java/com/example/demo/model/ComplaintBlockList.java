package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "complaint_block_list")
public class ComplaintBlockList {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    private Long id;

    @JsonIgnore
    @Lob
    @Column(columnDefinition = "TEXT")
    private String academicsJson;

    @JsonIgnore
    @Lob
    @Column(columnDefinition = "TEXT")
    private String sportsJson;

    @JsonIgnore
    @Lob
    @Column(columnDefinition = "TEXT")
    private String hostelJson;

    @JsonIgnore
    @Lob
    @Column(columnDefinition = "TEXT")
    private String categoryBlockedJson;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<String> getAcademics() {
        return readList(academicsJson);
    }

    public void setAcademics(List<String> academics) {
        this.academicsJson = writeList(academics);
    }

    public List<String> getSports() {
        return readList(sportsJson);
    }

    public void setSports(List<String> sports) {
        this.sportsJson = writeList(sports);
    }

    public List<String> getHostel() {
        return readList(hostelJson);
    }

    public void setHostel(List<String> hostel) {
        this.hostelJson = writeList(hostel);
    }

    public Map<String, Boolean> getCategoryBlocked() {
        if (categoryBlockedJson == null || categoryBlockedJson.isBlank()) return new HashMap<>();
        try {
            return OBJECT_MAPPER.readValue(categoryBlockedJson, new TypeReference<Map<String, Boolean>>() {});
        } catch (Exception ex) {
            return new HashMap<>();
        }
    }

    public void setCategoryBlocked(Map<String, Boolean> categoryBlocked) {
        try {
            this.categoryBlockedJson = OBJECT_MAPPER.writeValueAsString(categoryBlocked == null ? new HashMap<>() : categoryBlocked);
        } catch (Exception ex) {
            this.categoryBlockedJson = "{}";
        }
    }

    private List<String> readList(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    private String writeList(List<String> values) {
        try {
            return OBJECT_MAPPER.writeValueAsString(values == null ? Collections.emptyList() : values);
        } catch (Exception ex) {
            return "[]";
        }
    }
}
