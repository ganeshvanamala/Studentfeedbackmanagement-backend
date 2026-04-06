package com.example.demo.controller;

import com.example.demo.model.Form;
import com.example.demo.service.FormService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms")
@CrossOrigin(origins = "*")
public class FormController {

    @Autowired
    private FormService formService;

    @PostMapping
    public Form createForm(@RequestBody Form form) {
        return formService.createForm(form);
    }

    @GetMapping
    public List<Form> getAllForms() {
        return formService.getAllForms();
    }

    @PutMapping("/{id}/responses")
    public Form appendResponse(@PathVariable Long id, @RequestBody Map<String, Object> response) {
        return formService.appendResponse(id, response);
    }

    @DeleteMapping("/{id}")
    public void deleteForm(@PathVariable Long id) {
        formService.deleteById(id);
    }
}
