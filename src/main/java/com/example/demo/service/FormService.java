package com.example.demo.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.model.Form;
import com.example.demo.repository.FormRepository;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    public Form createForm(Form form) {
        if (form.getQuestions() == null) {
            form.setQuestions(new ArrayList<>());
        }
        if (form.getResponses() == null) {
            form.setResponses(new ArrayList<>());
        }
        if (form.getDepartmentIds() == null) {
            form.setDepartmentIds(new ArrayList<>());
        }
        if (form.getScopeIds() == null) {
            form.setScopeIds(new ArrayList<>());
        }
        return formRepository.save(form);
    }

    public List<Form> getAllForms() {
        return formRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Form appendResponse(Long formId, Map<String, Object> response) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));

        List<Map<String, Object>> responses = form.getResponses();
        if (responses == null) {
            responses = new ArrayList<>();
        }
        responses.add(response);
        form.setResponses(responses);
        return formRepository.save(form);
    }

    public void deleteById(Long formId) {
        if (!formRepository.existsById(formId)) {
            throw new RuntimeException("Form not found");
        }
        formRepository.deleteById(formId);
    }
}
