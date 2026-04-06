package com.example.demo.controller;

import com.example.demo.model.Faculty;
import com.example.demo.service.FacultyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "*")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public List<Faculty> getAll() {
        return facultyService.findAll();
    }

    @PostMapping
    public Faculty save(@RequestBody Faculty faculty) {
        return facultyService.save(faculty);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        facultyService.deleteById(id);
    }
}
